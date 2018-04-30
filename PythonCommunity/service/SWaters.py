# *- coding:utf8 *-
import sys
import os
sys.path.append(os.path.dirname(os.getcwd()))
import uuid
import DBSession
from models import model
from common.TransformToList import trans_params

class SWaters():
    def __init__(self):
        try:
            self.session = DBSession.db_session()
        except Exception as e:
            print "=====================message========================"
            print e.message
            print "=====================message========================"

    def get_water_list_by_uid(self, uid):
        water_list = None
        try:
            water_list = self.session.query(model.Waters.Wid, model.Waters.Wyear, model.Waters.Wmonth,
                                            model.Waters.Wpay, model.Waters.Wstatus).filter_by(Uid=uid).all()
        except Exception as e:
            print "=====================message========================"
            print e.message
            print "=====================message========================"
            self.session.rollback()
        finally:
            self.session.close()
        return water_list

    def update_water_by_wid(self, wid, waters):
        try:
            self.session.query(model.Waters).filter_by(Wid=wid).update(waters)
            self.session.commit()
            self.session.close()
            return True
        except Exception as e:
            print "=====================message========================"
            print e.message
            print "=====================message========================"
            self.session.rollback()
            self.session.close()
            return False

