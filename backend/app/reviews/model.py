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

def get_reviews(food: int) -> Review:
    return session.query(Review)\
        .join(Food)\
        .filter(Food.id == food)\
        .all()