package app.omnivore.omnivore.ui.library

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import app.omnivore.omnivore.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
  libraryViewModel: LibraryViewModel,
  onSearchClicked: () -> Unit,
  onSettingsIconClick: () -> Unit
) {
  val searchText: String by libraryViewModel.searchTextLiveData.observeAsState("")

  TopAppBar(
    title = {
        Text("Library")
    },
    colors = TopAppBarDefaults.topAppBarColors(
      containerColor = MaterialTheme.colorScheme.surfaceVariant
    ),
    actions = {
      IconButton(onClick = onSearchClicked) {
        Icon(
          imageVector = Icons.Filled.Search,
          contentDescription = null
        )
      }

      IconButton(onClick = onSettingsIconClick) {
        Icon(
          imageVector = Icons.Default.Settings,
          contentDescription = null
        )
      }
    }
  )
}


@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SearchField(
  searchText: String,
  onSearch: () -> Unit,
  onSearchTextChanged: (String) -> Unit,
  navController: NavHostController,
) {
  var showClearButton by remember { mutableStateOf(false) }
  val focusRequester = remember { FocusRequester() }

    TextField(
        modifier = Modifier
          .fillMaxWidth()
          .onFocusChanged { focusState ->
            showClearButton = (focusState.isFocused)
          }
          .focusRequester(focusRequester),
        value = searchText,
        onValueChange = onSearchTextChanged,
        placeholder = {
          Text(text = "Search")
        },
        leadingIcon = {
            IconButton(
                onClick = {
                    onSearchTextChanged("")
                    navController.popBackStack()
                }) {
                    Icon(
                        imageVector = androidx.compose.material.icons.Icons.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
        },
        trailingIcon = {
          AnimatedVisibility(
            visible = showClearButton,
            enter = fadeIn(),
            exit = fadeOut()
          ) {
            IconButton(onClick = { onSearchTextChanged("") }) {
              Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = null
              )
            }

          }
        },
        maxLines = 1,
        singleLine = true,
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = {
          onSearch()
        }),
      )

  LaunchedEffect(Unit) {
    focusRequester.requestFocus()
  }
}
