# SQLalchemy related imports
from sqlalchemy.ext.declarative import declarative_base

Base = declarative_base()

from app.entities.user import User
