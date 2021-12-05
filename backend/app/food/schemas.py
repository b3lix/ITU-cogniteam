# Projekt ITU
# Autori:
#   xbelko02 (Erik Belko)

# Pydantic related imports
from typing import Optional, List
from pydantic import BaseModel, constr, conint

# App related imports
from app.entities.food import FoodType

class ReviewCreateModel(BaseModel):
    description: Optional[constr(strip_whitespace=True, min_length=2)]
    positive_points: List[constr(strip_whitespace=True, min_length=2)]
    negative_points: List[constr(strip_whitespace=True, min_length=2)]
    price: Optional[conint(gt=0)]
    rating: conint(ge=0, le=5)

class CreateModel(BaseModel):
    name: constr(strip_whitespace=True, min_length=2)
    source: constr(strip_whitespace=True, min_length=2)
    description: Optional[constr(strip_whitespace=True, min_length=2)]
    type: FoodType
    barcode: Optional[conint(ge=0)]
    review: ReviewCreateModel

class FilterModel(BaseModel):
    type: Optional[FoodType]
    value: Optional[constr(strip_whitespace=True)] = ""
