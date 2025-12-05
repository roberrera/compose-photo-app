package com.roberrera.composephotoapp.network

import java.io.IOException

/**
 * A custom exception to indicate that a network request could not be made due to no connection.
 */
class NoConnectivityException : IOException(
    "No network available, please check your WiFi or Data connection"
)
