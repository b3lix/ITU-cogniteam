# Pydantic related imports
from typing import List

# Flask related imports
from flask_pydantic import validate
from flask_login import login_required, current_user

# App related imports
from app import make_response
from app.food import bp
from app.food.model import create_food, get_food, get_food_by_id, toggle_favourite
from app.food.schemas import CreateModel, FilterModel
from app.reviews.model import create_review
from app.entities.food import Food
from app.entities.review import Review

@bp.route("/create", methods=["POST"])
@login_required
@validate()
def create(body: CreateModel):
    food: Food = create_food(body.name, body.source, body.description, body.type, body.barcode)
    create_review(body.review.description, body.review.positive_points, body.review.negative_points, body.review.price, body.review.rating, food.id, current_user.id)

    return make_response(200)

@bp.route("/get", methods=["GET"])
@validate()
def get(query: FilterModel):
    user: int = None

    if current_user.is_authenticated:
        user = current_user.id

    return make_response(200, data={
        "food": [{
            "id": food[0].id,
            "name": food[0].name,
            "favourite": food[4] if len(food) > 4 else None,
            "source": food[0].source,
            "type": food[0].type,
            "description": food[0].description,
            "reviews": food[1],
            "average": {
                "price": round(food[3]),
                "rating": float(round(food[2], 1))
            }     
        } for food in get_food(query, user)]
    })
    
@bp.route("/get/<food_id>", methods=["GET"])
def get_one(food_id: int):
    food = get_food_by_id(food_id)

    if food == None:
        return make_response(404)

    return make_response(200, data={
        "name": food[0].name,
        "favourite": food[4],
        "source": food[0].source,
        "type": food[0].type,
        "description": food[0].description,
        "reviews": food[1],
        "average": {
            "price": round(food[3]),
            "rating": float(round(food[2], 1))
        }
    })

@bp.route("/favourite/<food_id>", methods=["POST"])
def favourite(food_id: int):
    if not current_user.is_authenticated:
        return make_response(401)

    toggle_favourite(food_id, current_user.id)
    return make_response(200)