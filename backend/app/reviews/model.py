# Projekt ITU
# Autori:
#   xslesa01 (Michal Šlesár)

# Standard library imports
from typing import List
from datetime import date

# App related imports
from app import session
from app.entities.review import Review
from app.entities.food import Food

def create_review(description: str, positive_points: List[str], negative_points: List[str], price: int, rating: int, food: int, user: int) -> Review:
    """Add review to database

    :param description:     Review description
    :param positive_points: List of positive points
    :param negative_points: List of negative points
    :param price:           Price that was the meal bought for
    :param rating:          User rating, 0-10
    :param food:            Food ID
    :param user:            User ID

    :return:                Created review
    """
    review = Review(description=description, positive_points=positive_points, negative_points=negative_points, date=date.today(), price=price, rating=rating, food_id=food, user_id=user)
    session.add(review)
    session.commit()
    session.refresh(review)
    return review

def update_review(id: int, description: str, positive_points: List[str], negative_points: List[str], price: int, rating: int) -> None:
    """Update review in database

    :param description:     Review description
    :param positive_points: List of positive points
    :param negative_points: List of negative points
    :param price:           Price that was the meal bought for
    :param rating:          User rating, 0-10

    :return:                Created review
    """
    review: Review = Review.query.get(id)

    if review == None:
        return
    
    review.description = description
    review.positive_points = positive_points
    review.negative_points = negative_points
    review.price = price
    review.rating = rating
    review.date = date.today()
    session.commit()

# Get all reviews of specific food
def get_reviews(food: int) -> Review:
    return session.query(Review)\
        .join(Food)\
        .filter(Food.id == food)\
        .order_by(Review.date.desc())\
        .all()

# Get all user reviews
def get_user_reviews(user: int) -> Review:
    return session.query(Review)\
        .join(Food)\
        .filter(Review.user_id == user)\
        .order_by(Review.date.desc())\
        .all()

# Get user review for specfic food
def get_user_review(food: int, user: int) -> Review:
    return session.query(Review)\
        .join(Food)\
        .filter(Review.user_id == user)\
        .filter(Food.id == food)\
        .first()