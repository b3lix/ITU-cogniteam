# Projekt ITU
# Autori:
#   xslesa01 (Michal Šlesár)

# SQLalchemy related imports
from sqlalchemy.ext.declarative import declarative_base

Base = declarative_base()

from app.entities.user import User
from app.entities.food import Food
from app.entities.review import Review
from app.entities.favourite import Favourite