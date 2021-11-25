# Pydantic related imports
from typing import Optional
from pydantic import BaseModel, constr

# App related imports
from app.entities.food import FoodType
from app.reviews.schemas import CreateModel as ReviewCreateModel

class CreateModel(BaseModel):
    name: constr(strip_whitespace=True, min_length=2)
    source: constr(strip_whitespace=True, min_length=2)
    description: Optional[constr(strip_whitespace=True, min_length=2)]
    type: FoodType
    review: ReviewCreateModel

class FilterModel(BaseModel):
    type: Optional[FoodType]
    value: str
