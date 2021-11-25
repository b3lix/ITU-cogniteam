# App related imports
from logging import Filter
from app import session
from app.entities.food import Food, FoodType
from app.entities.review import Review
from app.food.schemas import FilterModel

# SQLalchemy related imports
from sqlalchemy import func, or_

def create_food(name: str, source: str, description: str, type: FoodType) -> Food:
    """Add food to database

    :param name:        Name of the food
    :param source:      Source of the food (company name/restaurant name)
    :param description: Optional description of the food\
    :param type:        Type of the food

    :return:            Created food
    """
    food = Food(name=name, source=source, description=description, type=type)
    session.add(food)
    session.commit()
    session.refresh(food)
    return food

def get_food(filter: FilterModel):
    query = session.query(Food, func.count(Review.id).label("reviews"), func.avg(Review.rating), func.avg(Review.price))
    query = query.join(Review)
    query = query.group_by(Food.id)

    if(filter.type != None):
        query = query.filter(Food.type == filter.type)

    conditions = []
    values = [x.strip() for x in filter.value.split(",") if x.strip() != "" and x != None]
    for value in values:
        conditions.append(Food.name.ilike(f"%{value}%"))
        conditions.append(Food.source.ilike(f"%{value}%"))

    query = query.filter(or_(*conditions))
    query = query.order_by("reviews")
    return query.all()

def get_food_by_id(id: int):
    return session.query(Food, func.count(Review.id), func.avg(Review.rating), func.avg(Review.price))\
        .join(Review)\
        .filter(Food.id == id)\
        .group_by(Food.id)\
        .first()