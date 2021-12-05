# Projekt ITU
# Autori:
#   xslesa01 (Michal Šlesár)

# Flask related imports
from flask import Flask
from flask_cors import CORS
from flask_login import LoginManager

# SQLalchemy related imports
from sqlalchemy import create_engine
from sqlalchemy.orm import scoped_session, sessionmaker

# App related imports
from config import DATABASE_URI, SECRET_KEY

from app.entities import Base
from app.entities.user import User

# Initialize SQLalchemy engine
engine = create_engine(DATABASE_URI, convert_unicode = True)

# Create SQLalchemy session
session = scoped_session(sessionmaker(autocommit = False, autoflush = False, bind = engine))

# Setup query on entities
Base.query = session.query_property()
Base.metadata.create_all(bind=engine)

def make_response(status: int, data: object = ""):
    """Create flask response object

    :param status:  HTTP response status code
    :param data:    HTTP body data
    :return:        Flask response object
    """
    return (data, status)

def create_app():
    """Create flask application

    :return: Flask application object
    """
    app = Flask(__name__)
    app.secret_key = SECRET_KEY

    @app.after_request 
    def after_request_callback(response):
        session.close()
        return response

    # Setup CORS
    CORS(app, supports_credentials=True)

    # Setup flask login manager
    login_manager = LoginManager()
    @login_manager.user_loader
    def load_user(user_id):
        return User.query.get(user_id)

    login_manager.init_app(app)

    # Register route blueprints
    from app.auth import bp as auth_bp
    app.register_blueprint(auth_bp, url_prefix="/auth")

    from app.food import bp as food_bp
    app.register_blueprint(food_bp, url_prefix="/food")

    from app.reviews import bp as reviews_bp
    app.register_blueprint(reviews_bp, url_prefix="/reviews")

    return app
