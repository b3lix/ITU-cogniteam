# Pydantic related imports
from typing import List

# Flask related imports
from flask_pydantic import validate
from flask_login import login_required, current_user

# App related imports
from app import make_response, session
from app.reviews import bp
from app.entities.review import Review
from app.entities.food import Food
from app.reviews.model import create_review, get_reviews, get_user_review, get_user_reviews, update_review
from app.reviews.schemas import CreateModel, UpdateModel

@bp.route("/create", methods=["POST"])
@login_required
@validate()
def create(body: CreateModel):
    food: Food = Food.query.get(body.food)

    if food == None:
        return make_response(409)

    create_review(body.description, body.positive_points, body.negative_points, body.price, body.rating, body.food, current_user.id)
    return make_response(200)

@bp.route("/update", methods=["POST"])
@login_required
@validate()
def update(body: UpdateModel):
    review: Review = Review.query.get(body.id)

    if review == None:
        return make_response(409)

    update_review(body.id, body.description, body.positive_points, body.negative_points, body.price, body.rating)
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

@bp.route("/my", methods=["GET"])
def my():
    if not current_user.is_authenticated:
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
            "food": {
                "id": review.food.id,
                "name": review.food.name,
                "source": review.food.source,
                "type": review.food.type
            }
        } for review in get_user_reviews(current_user.id)]
    })

@bp.route("/my/<id>", methods=["GET"])
def my_one(id: int):
    food: Food = Food.query.get(id)

    if food == None:
        return make_response(404)

    if not current_user.is_authenticated:
        return make_response(404)

    review: Review = get_user_review(id, current_user.id)

    if review == None:
        return make_response(404)

    return make_response(200, data={
            "id": review.id,
            "description": review.description,
            "positive_points": review.positive_points,
            "negative_points": review.negative_points,
            "date": review.date,
            "price": review.price,
            "rating": review.rating   
        }
    )