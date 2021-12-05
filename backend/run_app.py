# Projekt ITU
# Autori:
#   xslesa01 (Michal Šlesár)

from app import create_app

# Inititialize application
if __name__ == "__main__":
    app = create_app()
    app.run(host = "localhost", debug = True, port = 8000)