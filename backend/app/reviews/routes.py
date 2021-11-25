# Pydantic related imports
from typing import List

# Flask related imports
from flask_pydantic import validate
from flask_login import login_required, current_user

# App related imports
from app import make_response
from app.reviews import bp
from app.entities.review import Review
from app.entities.food import Food
from app.reviews.model import create_review, get_reviews
from app.reviews.schemas import CreateModel

@bp.route("/create", methods=["POST"])
@login_required
@validate()
def create(body: CreateModel):
    food: Food = Food.query.get(body.food)

    if food == None:
        return make_response(409)

    create_review(body.description, body.positive_points, body.negative_points, body.price, body.rating, body.food, current_user.id)
    return make_response(200)

@bp.route("/get/<id>", methods=["GET"])
def get_one(id: int):
    food: Food = Food.query.get(id)

    if food == None:
        return make_response(404)

    return make_response(200, data={
        "reviews": [{
            "id": review.id,
            "description": review.description,
            "positive_points": review.positive_points,
            "negative_points": review.negative_points,
            "date": review.date,
            "price": review.price,
            "rating": review.rating,
            "user": review.user.username,    
        } for review in get_reviews(id)]
    })