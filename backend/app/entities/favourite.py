# SQLalchemy related imports
from sqlalchemy import Column, Integer, ForeignKey
from sqlalchemy.orm import relationship

# App related imports
from app.entities import Base
from app.entities.food import Food
from app.entities.user import User

class Favourite(Base):
    __tablename__               = "favourites"
    id: int                     = Column(Integer, primary_key=True, autoincrement=True)

    food_id: int                = Column(Integer, ForeignKey("food.id", ondelete="CASCADE"), nullable=False)
    user_id: int                = Column(Integer, ForeignKey("users.id", ondelete="CASCADE"), nullable=False)

    food = relationship(Food, foreign_keys=[food_id])
    user = relationship(User, foreign_keys=[user_id])