package com.example.saborforaneo.ui.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.saborforaneo.ui.screens.splash.PantallaSplash
import com.example.saborforaneo.ui.screens.onboarding.PantallaOnboarding
import com.example.saborforaneo.ui.screens.auth.PantallaLogin
import com.example.saborforaneo.ui.screens.auth.PantallaRegistro
import com.example.saborforaneo.ui.screens.auth.PantallaConfirmacionEmail
import com.example.saborforaneo.ui.screens.auth.PantallaRecuperarContrasena
import com.example.saborforaneo.ui.screens.home.PantallaInicio
import com.example.saborforaneo.ui.screens.search.PantallaBusqueda
import com.example.saborforaneo.ui.screens.detail.PantallaDetalleReceta
import com.example.saborforaneo.ui.screens.favorites.PantallaFavoritos
import com.example.saborforaneo.ui.screens.profile.PantallaPerfil
import com.example.saborforaneo.ui.screens.profile.PerfilViewModel
import com.example.saborforaneo.ui.screens.auth.PantallaTerminosCondiciones

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun GrafoNavegacion(
    controladorNav: NavHostController,
    pantallaInicio: String = Rutas.Splash.ruta,
    perfilViewModel: PerfilViewModel
) {
    NavHost(
        navController = controladorNav,
        startDestination = pantallaInicio,
        enterTransition = {
            fadeIn(animationSpec = tween(300)) +
                    slideInHorizontally(
                        initialOffsetX = { it },
                        animationSpec = tween(300)
                    )
        },
        exitTransition = {
            fadeOut(animationSpec = tween(300)) +
                    slideOutHorizontally(
                        targetOffsetX = { -it / 3 },
                        animationSpec = tween(300)
                    )
        },
        popEnterTransition = {
            fadeIn(animationSpec = tween(300)) +
                    slideInHorizontally(
                        initialOffsetX = { -it / 3 },
                        animationSpec = tween(300)
                    )
        },
        popExitTransition = {
            fadeOut(animationSpec = tween(300)) +
                    slideOutHorizontally(
                        targetOffsetX = { it },
                        animationSpec = tween(300)
                    )
        }
    ) {

        composable(
            route = Rutas.Splash.ruta,
            enterTransition = { fadeIn(animationSpec = tween(300)) },
            exitTransition = { fadeOut(animationSpec = tween(300)) }
        ) {
            PantallaSplash(
                navegarAOnboarding = {
                    controladorNav.navigate(Rutas.Onboarding.ruta) {
                        popUpTo(Rutas.Splash.ruta) { inclusive = true }
                    }
                },
                navegarALogin = {
                    controladorNav.navigate(Rutas.Login.ruta) {
                        popUpTo(Rutas.Splash.ruta) { inclusive = true }
                    }
                },
                navegarAInicio = {
                    controladorNav.navigate(Rutas.Inicio.ruta) {
                        popUpTo(Rutas.Splash.ruta) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = Rutas.Onboarding.ruta,
            enterTransition = { fadeIn(animationSpec = tween(300)) },
            exitTransition = { fadeOut(animationSpec = tween(300)) }
        ) {
            PantallaOnboarding(
                alFinalizar = {
                    controladorNav.navigate(Rutas.Login.ruta) {
                        popUpTo(Rutas.Onboarding.ruta) { inclusive = true }
                    }
                }
            )
        }

        composable(route = Rutas.Login.ruta) {
            PantallaLogin(
                navegarARegistro = {
                    controladorNav.navigate(Rutas.Registro.ruta)
                },
                navegarAInicio = {
                    controladorNav.navigate(Rutas.Inicio.ruta) {
                        popUpTo(Rutas.Login.ruta) { inclusive = true }
                    }
                },
                navegarARecuperarContrasena = {
                    controladorNav.navigate(Rutas.RecuperarContrasena.ruta)
                }
            )
        }

        composable(route = Rutas.Registro.ruta) {
            PantallaRegistro(
                navegarAtras = {
                    controladorNav.popBackStack()
                },
                navegarAConfirmacion = { email ->
                    controladorNav.navigate(Rutas.ConfirmacionEmail.crearRuta(email)) {
                        popUpTo(Rutas.Registro.ruta) { inclusive = true }
                    }
                },
                navegarATerminos = {
                    controladorNav.navigate(Rutas.TerminosCondiciones.ruta)
                }
            )
        }

        composable(route = Rutas.RecuperarContrasena.ruta) {
            PantallaRecuperarContrasena(
                navegarAtras = {
                    controladorNav.popBackStack()
                }
            )
        }

        composable(
            route = Rutas.ConfirmacionEmail.ruta,
            arguments = listOf(navArgument("email") { type = NavType.StringType })
        ) { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email") ?: ""
            PantallaConfirmacionEmail(
                email = email,
                navegarALogin = {
                    controladorNav.navigate(Rutas.Login.ruta) {
                        popUpTo(Rutas.ConfirmacionEmail.ruta) { inclusive = true }
                    }
                }
            )
        }

        composable(route = Rutas.TerminosCondiciones.ruta) {
            PantallaTerminosCondiciones(
                navegarAtras = {
                    controladorNav.popBackStack()
                }
            )
        }

        composable(route = Rutas.Inicio.ruta) {
            PantallaInicio(
                navegarADetalle = { recetaId ->
                    controladorNav.navigate(Rutas.DetalleReceta.crearRuta(recetaId))
                },
                navegarABusqueda = {
                    controladorNav.navigate(Rutas.Busqueda.ruta)
                },
                controladorNav = controladorNav
            )
        }

        composable(route = Rutas.Busqueda.ruta) {
            PantallaBusqueda(
                navegarADetalle = { recetaId ->
                    controladorNav.navigate(Rutas.DetalleReceta.crearRuta(recetaId))
                },
                navegarAtras = {
                    controladorNav.popBackStack()
                },
                controladorNav = controladorNav
            )
        }

        composable(
            route = Rutas.DetalleReceta.ruta,
            arguments = listOf(
                navArgument("recetaId") {
                    type = NavType.StringType
                }
            ),
            enterTransition = {
                fadeIn(animationSpec = tween(300)) +
                        slideInVertically(
                            initialOffsetY = { it },
                            animationSpec = tween(400)
                        )
            },
            exitTransition = {
                fadeOut(animationSpec = tween(300)) +
                        slideOutVertically(
                            targetOffsetY = { it },
                            animationSpec = tween(400)
                        )
            }
        ) { backStackEntry ->
            val recetaId = backStackEntry.arguments?.getString("recetaId") ?: ""
            PantallaDetalleReceta(
                recetaId = recetaId,
                navegarAtras = {
                    controladorNav.popBackStack()
                }
            )
        }

        composable(route = Rutas.Favoritos.ruta) {
            PantallaFavoritos(
                navegarADetalle = { recetaId ->
                    controladorNav.navigate(Rutas.DetalleReceta.crearRuta(recetaId))
                },
                controladorNav = controladorNav
            )
        }

        composable(route = Rutas.Perfil.ruta) {
            PantallaPerfil(
                navegarALogin = {
                    controladorNav.navigate(Rutas.Login.ruta) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                controladorNav = controladorNav,
                modeloVista = perfilViewModel
            )
        }
    }
}