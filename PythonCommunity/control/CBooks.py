# *- coding:utf8 *-
import sys
import os
sys.path.append(os.path.dirname(os.getcwd()))
from flask import request
import json
from config.requests import param_miss, system_error

class CBooks():
    def __init__(self):
        from service.SBooks import SBooks
        self.sbooks = SBooks()

    def book_list(self):
        book_list_all = self.sbooks.get_all_book()
        data = []
        for row in book_list_all:
            data_item = {}
            data_item["Bid"] = row.Bid
            data_item["Bno"] = row.Bno
            data_item["Bname"] = row.Bname
            data_item["Bstatus"] = row.Bstatus
            data_item["Btype"] = row.Btype
            data_item["Babo"] = row.Babo
            data.append(data_item)
        from config.requests import response_ok
        response_ok["data"] = data
        return response_ok

    def book_abo(self):
        args = request.args.to_dict()
        print "======================args==========================="
        print args
        print "======================args==========================="

        if "Bid" not in args:
            return param_miss
        Bid = args["Bid"]
        data = {}
        book_abo_all = self.sbooks.get_book_abo_by_bid(Bid)
        data["Bno"] = book_abo_all.Bno
        data["Bname"] = book_abo_all.Bname
        data["Bstatus"] = book_abo_all.Bstatus
        data["Btype"] = book_abo_all.Btype
        data["Babo"] = book_abo_all.Babo

        from config.requests import response_ok
        response_ok["data"] = data
        return response_ok

    def my_book(self):
        args = request.args.to_dict()
        print "======================args==========================="
        print args
        print "======================args==========================="

        if "Uid" not in args:
            return param_miss
        Uid = args["Uid"]

        book_list_all = self.sbooks.get_all_book_by_uid(Uid)
        data = []
        for row in book_list_all:
            data_item = {}
            data_item["Bid"] = row.Bid
            raw = self.sbooks.get_book_abo_by_bid(row.Bid)
            data_item["Bno"] = raw.Bno
            data_item["Bname"] = raw.Bname
            data_item["Bstatus"] = raw.Bstatus
            data_item["Btype"] = raw.Btype
            data_item["Babo"] = raw.Babo
            data.append(data_item)
        from config.requests import response_ok
        response_ok["data"] = data
        return response_ok

    def update_status(self):
        args = request.args.to_dict()
        print "======================args==========================="
        print args
        print "======================args==========================="

        if "Uid" not in args or "Bid" not in args:
            return param_miss

        Uid = args["Uid"]
        Bid = args["Bid"]

        data = request.data
        print "======================data==========================="
        print data
        print "======================data==========================="
        data = json.loads(data)

        if "Bstatus" not in data:
            return param_miss

        Bstatus = data["Bstatus"]
        if Bstatus == 301:
            # 预约逻辑
            insert_ul = self.sbooks.add_user_book(Uid, Bid)
            if not insert_ul:
                return system_error
            bstatus = {}
            bstatus["Bstatus"] = 302
            update_status = self.sbooks.update_bstatus(Bid, bstatus)
            if not update_status:
                return system_error
        elif Bstatus == 302:
            # 结算逻辑
            bstatus = {}
            bstatus["Bstatus"] = 301
            update_status = self.sbooks.update_bstatus(Bid, bstatus)
            if not update_status:
                return system_error
        else:
            return system_error

        from config.requests import response_ok
        return response_ok