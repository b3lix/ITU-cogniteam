# Standard library imports
from typing import List, Optional

# Pydantic related imports
from pydantic import BaseModel, constr, conint

class CreateModel(BaseModel):
    food: int
    description: Optional[constr(strip_whitespace=True, min_length=2)]
    positive_points: List[constr(strip_whitespace=True, min_length=2)]
    negative_points: List[constr(strip_whitespace=True, min_length=2)]
    price: Optional[conint(gt=0)]
    rating: conint(ge=0, le=10)