# *- coding:utf8 *-
import sys
import os
sys.path.append(os.path.dirname(os.getcwd()))
from flask import request
import json
from config.requests import param_miss, system_error

class CLocations():
    def __init__(self):
        from service.SLocations import SLocations
        self.slocations = SLocations()

    def location_list(self):
        location_list_all = self.slocations.get_all_location()
        data = []
        for row in location_list_all:
            data_item = {}
            data_item["Lid"] = row.Lid
            data_item["Lno"] = row.Lno
            data_item["Lstatus"] = row.Lstatus
            data_item["Labo"] = row.Labo
            data.append(data_item)

        from config.requests import response_ok
        response_ok["data"] = data
        return response_ok

    def my_location(self):
        args = request.args.to_dict()
        print "======================args==========================="
        print args
        print "======================args==========================="

        if "Uid" not in args:
            return param_miss

        Uid = args["Uid"]

        location_list_all = self.slocations.get_location_by_uid(Uid)
        data = []
        for row in location_list_all:
            data_item = {}
            data_item["Lid"] = row.Lid
            raw = self.slocations.get_location_by_lid(row.Lid)
            data_item["Lno"] = raw.Lno
            data_item["Lstatus"] = raw.Lstatus
            data.append(data_item)

        from config.requests import response_ok
        response_ok["data"] = data
        return response_ok

    def update_lstatus(self):
        args = request.args.to_dict()
        print "======================args==========================="
        print args
        print "======================args==========================="

        if "Uid" not in args or "Lid" not in args:
            return param_miss

        Uid = args["Uid"]
        Lid = args["Lid"]

        data = request.data
        print "======================data==========================="
        print data
        print "======================data==========================="
        data = json.loads(data)

        if "Lstatus" not in data:
            return param_miss

        Lstatus = data["Lstatus"]
        if Lstatus == 501:
            #预约逻辑
            insert_ul = self.slocations.add_user_location(Uid, Lid)
            if not insert_ul:
                return system_error
            lstatus = {}
            lstatus["Lstatus"] = 502
            update_status = self.slocations.update_lstatus(Lid, lstatus)
            if not update_status:
                return system_error
        elif Lstatus == 502:
            #结算逻辑
            lstatus = {}
            lstatus["Lstatus"] = 501
            update_status = self.slocations.update_lstatus(Lid, lstatus)
            if not update_status:
                return system_error
        else:
            return system_error

        from config.requests import response_ok
        return response_ok