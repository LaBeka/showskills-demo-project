<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>About productEntity</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css"
          integrity="sha384-xOolHFLEh07PJGoPkLv1IbcEPTNtaed2xpHsD9ESMhqIYd0nLMwNLD69Npy4HI+N" crossorigin="anonymous">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
    <script src="https://code.jquery.com/jquery-3.6.4.js" integrity="sha256-a9jBBRygX1Bh5lt8GZjXDzyOB+bWve9EiO7tROUtj/E=" crossorigin="anonymous"></script>

    <link rel="stylesheet" href="css/bootstrap.min.css">
    <link rel="stylesheet" href="css/style.css">
    <link rel="stylesheet" href="css/cart-style.css">
    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js" integrity="sha384-DfXdz2htPH0lsSSs5nCTpuj/zy4C+OGpamoFVy38MVBnE+IbbVYUew+OrCXaRkfj" crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-ho+j7jyWK8fNQe+A12Hb8AhRq26LrZ/JpcUGGOn+Y7RsweNrtN/tE3MoK7ZeZDyx" crossorigin="anonymous"></script>

</head>
<body>
<script>
    $(()=>{
        goDetailsProduct();
    })
</script>
<header class="header">
    <nav class="navbar navbar-expand-lg py-3">
        <div class="container">
            <a class="navbar-brand" href="#">
                <img src="images/logo.png" alt="Builder">
            </a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#menuExpanded">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="menuExpanded">
                <ul class="navbar-nav ms-auto">
                    <li class="nav-item mx-2">
                        <a class="nav-link" href="#">Brands</a>
                    </li>
                    <li class="nav-item mx-2">
                        <a class="nav-link" href="#">New products</a>
                    </li>

                    <li class="nav-item mx-2">
                        <a class="nav-link" href="#">Categories</a>
                    </li>
                    <li class="nav-item mx-2">
                        <a class="nav-link" href="#">Lowered price</a>
                    </li>
                </ul>
                <form action="/logout" method="POST">
                    <button type="submit" class="btn btn-primary">Logout</button>
                </form>
                <div class="shopping-header" id="shopping-header">
                    <img src="images/cart.png"  alt="cart-icon">
                    <span class="cartQuantity">0</span>
                </div>
            </div>
        </div>
    </nav>
</header>
<div class="container">
    <div class="container-search">
        <div class="jss124" data-testid="SearchBar_searchbar">
            <form id="search-in-word">
                <div class="custom-file">
                    <input name="in-word" id="in-word" placeholder="Search products or brands" type="text" maxlength="60" value="">
                    <button tabindex="0" type="submit" aria-label="Search">
                        <span class="MuiTypography-root MuiTypography-srOnly">search</span>
                    </button>

                </div>
            </form>
        </div>
    </div>
    <h1 class="text-center mb-4 mt-5 main-title">Products</h1>

    <div class="bg-light py-5">
    </div>
</div>
<div class="shop-cart" id="shop-cart">
    <h1>Cart</h1>
    <ul class="listCard">
                <li>
                    <div>
                        <img src="${i.imagePath}" alt="${i.productName}">
                    </div>
                    <div>${i.productName}</div>
                    <div>${i.productPrice}</div>

                    <div>
                        <form action="/minusOneProduct" method="POST" id="addItemCart">
                            <input type="hidden" id="name" name="shopCartItemId" value="${i.shopCartItemId}"/>
                            <button type="submit">-</button>
                        </form>
                        <div class="count">${i.qty}</div>
                        <form action="/plusOneProduct" method="POST" id="addItemCart">
                            <input type="hidden" id="name" name="shopCartItemId" value="${i.shopCartItemId}"/>
                            <button type="submit">+</button>
                        </form>
                    </div>
                </li>
    </ul>
    <div class="checkout">
        <#--        click to the total sum to go to orders page-->
        <div class="total" onclick="createOrder()">0</div>
        <div class="close">close</div>
    </div>
</div>

<script src="https://unpkg.com/axios/dist/axios.min.js"></script>
<script src="js/productEntity-detail.js"></script>

</body>
</html>