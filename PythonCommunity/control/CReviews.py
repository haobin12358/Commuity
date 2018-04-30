# *- coding:utf8 *-
import sys
import os
sys.path.append(os.path.dirname(os.getcwd()))
from flask import request
import json
from config.requests import param_miss, system_error

class CReviews():
    def __init__(self):
        from service.SReviews import SReviews
        self.sreviews = SReviews()
        from service.SUsers import SUsers
        self.susers = SUsers()

    def reviews_list(self):
        args = request.args.to_dict()
        print "======================args==========================="
        print args
        print "======================args==========================="

        if "Cid" not in args:
            return param_miss

        Cid = args["Cid"]
        all_review = self.sreviews.get_review_by_cid(Cid)
        data = []
        for row in all_review:
            data_item = {}
            data_item["Uid"] = row.Uid
            data_item["Uname"] = self.susers.get_uname_by_uid(row.Uid)
            data_item["RUid"] = row.RUid
            data_item["RUname"] = self.susers.get_uname_by_uid(row.RUid)
            data_item["Rabo"] = row.Rabo
            data_item["Rtime"] = row.Rtime
            data.append(data_item)

        from config.requests import response_ok
        response_ok["data"] = data
        return response_ok

    def add_review(self):
        args = request.args.to_dict()
        print "======================args==========================="
        print args
        print "======================args==========================="

        if "Uid" not in args or "Cid" not in args:
            return param_miss

        Uid = args["Uid"]
        Cid = args["Cid"]
        RUid = args["RUid"]

        data = request.data
        print "======================data==========================="
        print data
        print "======================data==========================="
        data = json.loads(data)

        if "Rabo" not in data:
            return param_miss
        Rabo = data["Rabo"]
        add_review = self.sreviews.new_review(Uid, Cid, RUid, Rabo)
        if not add_review:
            return system_error
        from config.requests import response_ok
        return response_ok