# Standard library imports
from typing import List
from datetime import date

# SQLalchemy related imports
from sqlalchemy import Column, String, Integer, ForeignKey, ARRAY, Date
from sqlalchemy.orm import relationship

# App related imports
from app.entities import Base
from app.entities.food import Food
from app.entities.user import User

class Review(Base):
    __tablename__               = "reviews"
    id: int                     = Column(Integer, primary_key=True, autoincrement=True)
    description: str            = Column(String(64), index=True, unique=True)
    positive_points: List[str]  = Column(ARRAY(String(64)))
    negative_points: List[str]  = Column(ARRAY(String(64)))
    date: date                  = Column(Date)
    price: int                  = Column(Integer, nullable=True)
    rating: int                 = Column(Integer)

    food_id: int                = Column(Integer, ForeignKey("food.id", ondelete="CASCADE"), nullable=False)
    user_id: int                = Column(Integer, ForeignKey("users.id", ondelete="CASCADE"), nullable=False)

    food = relationship(Food, foreign_keys=[food_id])
    user = relationship(User, foreign_keys=[user_id])