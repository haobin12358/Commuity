# *- coding:utf8 *-
import sys
import os
sys.path.append(os.path.dirname(os.getcwd()))
from flask import request
import json
from config.requests import param_miss, system_error

class CUsers():
    def __init__(self):
        from service.SUsers import SUsers
        self.susers = SUsers()

    def user_info(self):
        args = request.args.to_dict()
        print "======================args==========================="
        print args
        print "======================args==========================="

        if "Uid" not in args:
            return param_miss

        Uid = args["Uid"]

        user_info = self.susers.get_userinfo_by_uid(Uid)

        Utel = user_info.Utel
        Uname = user_info.Uname
        Ucardno = user_info.Ucardno
        Usex = user_info.Usex
        Ucoin = user_info.Ucoin
        Ulive = user_info.Ulive

        from config.requests import response_ok
        response_ok["data"] = {}
        response_ok["data"]["Utel"] = Utel
        response_ok["data"]["Uname"] = Uname
        response_ok["data"]["Ucardno"] = Ucardno
        response_ok["data"]["Usex"] = Usex
        response_ok["data"]["Ucoin"] = Ucoin
        response_ok["data"]["Ulive"] = Ulive

        return response_ok

    def register(self):
        data = request.data
        print "======================data==========================="
        print data
        print "======================data==========================="
        data = json.loads(data)
        if "Utel" not in data or "Upwd" not in data:
            return param_miss

        list_utel = self.susers.get_all_user_tel()

        if list_utel == False:
            return system_error

        if data["Utel"] in list_utel:
            from config.requests import repeat_tel
            return repeat_tel

        is_register = self.susers.regist_user(data["Utel"], data["Upwd"])
        if is_register:
            from config.requests import register_ok
            return register_ok
        else:
            return system_error

    def login(self):
        data = request.data
        print "======================data==========================="
        print data
        print "======================data==========================="
        data = json.loads(data)
        if "Utel" not in data or "Upwd" not in data:
            return param_miss

        Utel = data["Utel"]
        list_utel = self.susers.get_all_user_tel()

        if list_utel == False:
            return system_error

        if Utel not in list_utel:
            from config.requests import no_tel
            return no_tel

        upwd = self.susers.get_upwd_by_utel(Utel)
        if upwd != data["Upwd"]:
            from config.requests import wrong_pwd
            return wrong_pwd

        Uid = self.susers.get_uid_by_utel(Utel)

        from config.requests import login_ok
        login_ok["data"] = {}
        login_ok["data"]["Uid"] = Uid

        return login_ok

    def update_info(self):
        args = request.args.to_dict()
        print "======================args==========================="
        print args
        print "======================args==========================="
        data = request.data
        print "======================data==========================="
        print data
        print "======================data==========================="
        data = json.loads(data)
        if "Uid" not in args:
            return param_miss

        users = {}
        Uid = args["Uid"]
        if "Uname" in data:
            Uname = data["Uname"]
            users["Uname"] = Uname
        if "Usex" in data:
            Usex = data["Usex"]
            users["Usex"] = Usex
        if "Ulive" in data:
            Ulive = data["Ulive"]
            users["Ulive"] = Ulive
        if "Ucardno" in data:
            Ucardno = data["Ucardno"]
            users["Ucardno"] = Ucardno
        if "Ucoin" in data:
            Ucoin = data["Ucoin"]
            users["Ucoin"] = Ucoin

        if users == {}:
            return param_miss

        update_info = self.susers.update_userinfo_by_uid(users, Uid)

        if not update_info:
            return system_error

        from config.requests import success_update_uinfo
        return success_update_uinfo