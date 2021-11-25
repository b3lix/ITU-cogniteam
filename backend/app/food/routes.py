# Pydantic related imports
from typing import List

# Flask related imports
from flask_pydantic import validate
from flask_login import login_required, current_user

# App related imports
from app import make_response
from app.food import bp
from app.food.model import create_food, get_food, get_food_by_id
from app.food.schemas import CreateModel, FilterModel
from app.reviews.model import create_review
from app.entities.food import Food
from app.entities.review import Review

@bp.route("/create", methods=["POST"])
@login_required
@validate()
def create(body: CreateModel):
    food: Food = create_food(body.name, body.source, body.description, body.type)
    create_review(body.review.description, body.review.positive_points, body.review.negative_points, body.review.price, body.review.rating, food.id, current_user.id)

    return make_response(200)

@bp.route("/get", methods=["GET"])
@validate()
def get(query: FilterModel):
    return make_response(200, data={
        "food": [{
            "id": food[0].id,
            "name": food[0].name,
            "source": food[0].source,
            "type": food[0].type,
            "description": food[0].description,
            "reviews": food[1],
            "average": {
                "price": round(food[3]),
                "rating": float(round(food[2], 1))
            }     
        } for food in get_food(query)]
    })
    
@bp.route("/get/<id>", methods=["GET"])
def get_one(id: int):
    food = get_food_by_id(id)

    if food == None:
        return make_response(404)

    return make_response(200, data={
        "name": food[0].name,
        "source": food[0].source,
        "type": food[0].type,
        "description": food[0].description,
        "reviews": food[1],
        "average": {
            "price": round(food[3]),
            "rating": float(round(food[2], 1))
        }
    })