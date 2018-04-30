# *- coding:utf8 *-
import sys
import os
sys.path.append(os.path.dirname(os.getcwd()))
from flask import request
import json
from config.requests import param_miss, system_error

class CWaters():
    def __init__(self):
        from service.SWaters import SWaters
        self.swaters = SWaters()

        from service.SUsers import SUsers
        self.susers = SUsers()

    def my_water(self):
        args = request.args.to_dict()
        print "======================args==========================="
        print args
        print "======================args==========================="

        if "Uid" not in args:
            return param_miss

        Uid = args["Uid"]

        my_water_list = self.swaters.get_water_list_by_uid(Uid)
        if not my_water_list:
            return system_error
        data = []
        for row in my_water_list:
            data_item = {}
            data_item["Wid"] = row.Wid
            data_item["Wyear"] = row.Wyear
            data_item["Wmonth"] = row.Wmonth
            data_item["Wpay"] = row.Wpay
            data_item["Wstatus"] = row.Wstatus
            data.append(data_item)

        from config.requests import response_ok
        response_ok["data"] = data
        return response_ok

    def update_wstatus(self):
        args = request.args.to_dict()
        print "======================args==========================="
        print args
        print "======================args==========================="
        data = request.data
        print "======================args==========================="
        print data
        print "======================args==========================="
        data = json.loads(data)

        if "Uid" not in args or "Wid" not in args:
            return param_miss

        Uid = args["Uid"]
        Wid = args["Wid"]
        if "Wpay" not in data or "Wstatus" not in data:
            return param_miss

        Wstatus = data["Wstatus"]
        Wpay = data["Wpay"]
        if Wstatus != 601:
            return
        update_water = {}
        update_water["Wstatus"] = 602

        Ucoin = self.susers.get_ucoin_by_uid(Uid)
        Ucoin_update = Ucoin - Wpay
        update_users = {}
        update_users["Ucoin"] = Ucoin_update

        update_ucoin = self.susers.update_ucoin_by_uid(Uid, update_users)
        if not update_ucoin:
            return system_error

        update_water_status = self.swaters.update_water_by_wid(Wid, update_water)
        if not update_water_status:
            return system_error
        from config.requests import response_ok
        return response_ok