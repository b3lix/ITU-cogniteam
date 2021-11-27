# Standard library imports
import enum

# SQLalchemy related imports
from sqlalchemy import Column, String, Integer, Enum

# App related imports
from app.entities import Base

class FoodType(enum.IntEnum):
    shop = 0
    restaurant = 1

class Food(Base):
    __tablename__     = "food"
    id: int           = Column(Integer, primary_key = True, autoincrement = True)
    name: str         = Column(String(64), index = True, unique = True)
    source: str       = Column(String(128))
    description: str  = Column(String(128), nullable=True)
    type: FoodType    = Column(Enum(FoodType))
    barcode: int      = Column(Integer, nullable=True)
