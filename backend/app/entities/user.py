# Projekt ITU
# Autori:
#   xslesa01 (Michal Šlesár)
#   xbelko02 (Erik Belko)
#   xnosko06 (Matúš Nosko)

# Flask related imports
from flask_login import UserMixin

# SQLalchemy related imports
from sqlalchemy import Column, String, Integer

# App related imports
from app.entities import Base

class User(UserMixin, Base):
    __tablename__  = "users"
    id: int        = Column(Integer, primary_key = True, autoincrement = True)
    username: str  = Column(String(64), index = True, unique = True)
    password: str  = Column(String(128))
