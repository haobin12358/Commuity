# *- coding:utf8 *-

from flask import Flask
import flask_restful
from apis.AUsers import AUsers
from apis.ABooks import ABooks
from apis.ALocations import ALocations
from apis.AWaters import AWaters
from apis.ACards import ACards
from apis.AReviews import AReviews

app = Flask(__name__)
api = flask_restful.Api(app)

api.add_resource(AUsers, "/wjs/users/<string:users>")
api.add_resource(ABooks, "/wjs/books/<string:books>")
api.add_resource(ALocations, "/wjs/locations/<string:locations>")
api.add_resource(AWaters, "/wjs/waters/<string:waters>")
api.add_resource(ACards, "/wjs/cards/<string:cards>")
api.add_resource(AReviews, "/wjs/reviews/<string:reviews>")

if __name__ == '__main__':
    app.run('0.0.0.0', 8112, debug=True)
