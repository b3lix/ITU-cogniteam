# Projekt ITU
# Autori:
#   xnosko06 (Matúš Nosko)

# Pydantic related imports
from pydantic import BaseModel, constr

class RegisterModel(BaseModel):
    username: constr(strip_whitespace=True, min_length=3)
    password: constr(strip_whitespace=True, min_length=3)
    
class LoginModel(BaseModel):
    username: constr(strip_whitespace=True, min_length=3)
    password: constr(strip_whitespace=True, min_length=3)
