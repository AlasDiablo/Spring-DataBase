package fr.alasdiablo.spring.database.api.function

import fr.alasdiablo.spring.database.api.TableDefinitions
import fr.alasdiablo.spring.database.data.Pair
import java.util.function.Supplier

interface BiSupplier<T> : Supplier<Pair<TableDefinitions.Type, T>>