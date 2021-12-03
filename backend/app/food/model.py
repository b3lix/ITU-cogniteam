# App related imports
from app import session
from app.entities.food import Food, FoodType
from app.entities.review import Review
from app.food.schemas import FilterModel

# SQLalchemy related imports
from sqlalchemy import func, or_, and_, cast, String

# App related imports
from app.entities.favourite import Favourite

def create_food(name: str, source: str, description: str, type: FoodType, barcode: int) -> Food:
    """Add food to database

    :param name:        Name of the food
    :param source:      Source of the food (company name/restaurant name)
    :param description: Optional description of the food\
    :param type:        Type of the food
    :barcode barcode:   Food barcode

    :return:            Created food
    """
    food = Food(name=name, source=source, description=description, type=type, barcode=barcode)
    session.add(food)
    session.commit()
    session.refresh(food)
    return food

def get_food(filter: FilterModel, user: int):
    query = session.query(Food, func.count(Review.id).label("reviews"), func.avg(Review.rating), func.avg(Review.price))
    query = query.join(Review, Review.food_id == Food.id)

    if(filter.type != None):
        query = query.filter(Food.type == filter.type)

    # If user is logged in, order by favourite meals
    if user != None:
        query = query.add_columns(Favourite.id)

        if filter.value == "":
            query = query.join(Favourite, and_(Favourite.food_id == Food.id, Favourite.user_id == user))
        else:
            query = query.outerjoin(Favourite, and_(Favourite.food_id == Food.id, Favourite.user_id == user))

        query = query.order_by(Favourite.id, "reviews")
        query = query.group_by(Food.id, Favourite.id)
    else:
        query = query.order_by("reviews")
        query = query.group_by(Food.id)

    # If filter is provided
    if filter.value != "":
        conditions = []
        values = [x.strip() for x in filter.value.split(",") if x.strip() != "" and x != None]
        for value in values:
            conditions.append(Food.name.ilike(f"%{value}%"))
            conditions.append(Food.source.ilike(f"%{value}%"))
            conditions.append(Food.barcode.cast(String).ilike(f"%{value}%"))

        query = query.filter(or_(*conditions))

    return query.all()

def get_food_by_id(id: int):
    return session.query(Food, func.count(Review.id), func.avg(Review.rating), func.avg(Review.price), Favourite.id)\
        .outerjoin(Favourite)\
        .join(Review)\
        .filter(Food.id == id)\
        .group_by(Food.id, Favourite.id)\
        .first()

def toggle_favourite(food: int, user: int) -> None:
    favourite: Favourite = Favourite.query.filter(Favourite.user_id == user).filter(Favourite.food_id == food).first()

    if favourite != None:
        session.delete(favourite)
        session.commit()
        return

    if get_food_by_id(food) != None:
        favourite = Favourite(user_id=user, food_id=food)
        session.add(favourite)
        session.commit()

    return