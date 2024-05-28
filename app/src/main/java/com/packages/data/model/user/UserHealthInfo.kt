package com.packages.data.model.user

import com.packages.main.model.user.DietaryRequirements

data class UserHealthInfo(
  var height: Float,
  var weight: Float,
  var dietaryRequirements: DietaryRequirements
)
