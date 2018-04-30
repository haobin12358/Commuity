# *- coding:utf8 *-
import sys
import os
sys.path.append(os.path.dirname(os.getcwd()))
import uuid
import DBSession
from models import model
from common.TransformToList import trans_params

class SLocations():
    def __init__(self):
        try:
            self.session = DBSession.db_session()
        except Exception as e:
            print "=====================message========================"
            print e.message
            print "=====================message========================"

    def get_all_location(self):
        all_location = None
        try:
            all_location = self.session.query(model.Locations.Lid, model.Locations.Lno, model.Locations.Lstatus,
                                              model.Locations.Labo).all()
        except Exception as e:
            self.session.rollback()
            print "=====================message========================"
            print e.message
            print "=====================message========================"
        finally:
            self.session.close()
        return all_location

    def get_location_by_uid(self, uid):
        all_location = None
        try:
            all_location = self.session.query(model.ULocations.Lid)\
                .filter_by(Uid=uid).all()
        except Exception as e:
            self.session.rollback()
            print "=====================message========================"
            print e.message
            print "=====================message========================"
        finally:
            self.session.close()
        return all_location

    def get_location_by_lid(self, lid):
        all_location = None
        try:
            all_location = self.session.query(model.Locations.Lno, model.Locations.Lstatus, model.Locations.Labo)\
                .filter_by(Lid=lid).first()
        except Exception as e:
            self.session.rollback()
            print "=====================message========================"
            print e.message
            print "=====================message========================"
        finally:
            self.session.close()
        return all_location

    def add_user_location(self, uid, lid):
        """
        :param uid:
        :param lid:
        :return:
        """
        try:
            new_location_user = model.ULocations()
            new_location_user.ULid = str(uuid.uuid4())
            new_location_user.Uid = uid
            new_location_user.Lid = lid
            new_location_user.ULtimemin = "timewrong"
            new_location_user.ULtimemax = None
            self.session.add(new_location_user)
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

    def update_lstatus(self, lid, locations):
        try:
            self.session.query(model.Locations).filter_by(Lid=lid).update(locations)
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