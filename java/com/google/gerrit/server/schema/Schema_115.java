begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2015 The Android Open Source Project
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// Licensed under the Apache License, Version 2.0 (the "License");
end_comment

begin_comment
comment|// you may not use this file except in compliance with the License.
end_comment

begin_comment
comment|// You may obtain a copy of the License at
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// http://www.apache.org/licenses/LICENSE-2.0
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// Unless required by applicable law or agreed to in writing, software
end_comment

begin_comment
comment|// distributed under the License is distributed on an "AS IS" BASIS,
end_comment

begin_comment
comment|// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
end_comment

begin_comment
comment|// See the License for the specific language governing permissions and
end_comment

begin_comment
comment|// limitations under the License.
end_comment

begin_package
DECL|package|com.google.gerrit.server.schema
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|schema
package|;
end_package

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Preconditions
operator|.
name|checkState
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|config
operator|.
name|ConfigUtil
operator|.
name|storeSection
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|util
operator|.
name|Objects
operator|.
name|requireNonNull
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Strings
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|extensions
operator|.
name|client
operator|.
name|DiffPreferencesInfo
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|extensions
operator|.
name|client
operator|.
name|DiffPreferencesInfo
operator|.
name|Whitespace
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|extensions
operator|.
name|client
operator|.
name|Theme
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|reviewdb
operator|.
name|client
operator|.
name|Account
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|reviewdb
operator|.
name|server
operator|.
name|ReviewDb
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|GerritPersonIdent
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|config
operator|.
name|AllUsersName
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|extensions
operator|.
name|events
operator|.
name|GitReferenceUpdated
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|git
operator|.
name|GitRepositoryManager
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|git
operator|.
name|UserConfigSections
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|git
operator|.
name|meta
operator|.
name|MetaDataUpdate
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|patch
operator|.
name|PatchListKey
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|jdbc
operator|.
name|JdbcSchema
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|server
operator|.
name|OrmException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Inject
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Provider
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|ResultSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|ResultSetMetaData
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|SQLException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|Statement
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|errors
operator|.
name|ConfigInvalidException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|BatchRefUpdate
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|NullProgressMonitor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|PersonIdent
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|Repository
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|revwalk
operator|.
name|RevWalk
import|;
end_import

begin_class
DECL|class|Schema_115
specifier|public
class|class
name|Schema_115
extends|extends
name|SchemaVersion
block|{
DECL|field|mgr
specifier|private
specifier|final
name|GitRepositoryManager
name|mgr
decl_stmt|;
DECL|field|allUsersName
specifier|private
specifier|final
name|AllUsersName
name|allUsersName
decl_stmt|;
DECL|field|serverUser
specifier|private
specifier|final
name|PersonIdent
name|serverUser
decl_stmt|;
annotation|@
name|Inject
DECL|method|Schema_115 ( Provider<Schema_114> prior, GitRepositoryManager mgr, AllUsersName allUsersName, @GerritPersonIdent PersonIdent serverUser)
name|Schema_115
parameter_list|(
name|Provider
argument_list|<
name|Schema_114
argument_list|>
name|prior
parameter_list|,
name|GitRepositoryManager
name|mgr
parameter_list|,
name|AllUsersName
name|allUsersName
parameter_list|,
annotation|@
name|GerritPersonIdent
name|PersonIdent
name|serverUser
parameter_list|)
block|{
name|super
argument_list|(
name|prior
argument_list|)
expr_stmt|;
name|this
operator|.
name|mgr
operator|=
name|mgr
expr_stmt|;
name|this
operator|.
name|allUsersName
operator|=
name|allUsersName
expr_stmt|;
name|this
operator|.
name|serverUser
operator|=
name|serverUser
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|migrateData (ReviewDb db, UpdateUI ui)
specifier|protected
name|void
name|migrateData
parameter_list|(
name|ReviewDb
name|db
parameter_list|,
name|UpdateUI
name|ui
parameter_list|)
throws|throws
name|OrmException
throws|,
name|SQLException
block|{
name|Map
argument_list|<
name|Account
operator|.
name|Id
argument_list|,
name|DiffPreferencesInfo
argument_list|>
name|imports
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
try|try
init|(
name|Statement
name|stmt
init|=
operator|(
operator|(
name|JdbcSchema
operator|)
name|db
operator|)
operator|.
name|getConnection
argument_list|()
operator|.
name|createStatement
argument_list|()
init|;
name|ResultSet
name|rs
operator|=
name|stmt
operator|.
name|executeQuery
argument_list|(
literal|"SELECT * FROM account_diff_preferences"
argument_list|)
init|)
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|availableColumns
init|=
name|getColumns
argument_list|(
name|rs
argument_list|)
decl_stmt|;
while|while
condition|(
name|rs
operator|.
name|next
argument_list|()
condition|)
block|{
name|Account
operator|.
name|Id
name|accountId
init|=
operator|new
name|Account
operator|.
name|Id
argument_list|(
name|rs
operator|.
name|getInt
argument_list|(
literal|"id"
argument_list|)
argument_list|)
decl_stmt|;
name|DiffPreferencesInfo
name|prefs
init|=
operator|new
name|DiffPreferencesInfo
argument_list|()
decl_stmt|;
if|if
condition|(
name|availableColumns
operator|.
name|contains
argument_list|(
literal|"context"
argument_list|)
condition|)
block|{
name|prefs
operator|.
name|context
operator|=
operator|(
name|int
operator|)
name|rs
operator|.
name|getShort
argument_list|(
literal|"context"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|availableColumns
operator|.
name|contains
argument_list|(
literal|"expand_all_comments"
argument_list|)
condition|)
block|{
name|prefs
operator|.
name|expandAllComments
operator|=
name|toBoolean
argument_list|(
name|rs
operator|.
name|getString
argument_list|(
literal|"expand_all_comments"
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|availableColumns
operator|.
name|contains
argument_list|(
literal|"hide_line_numbers"
argument_list|)
condition|)
block|{
name|prefs
operator|.
name|hideLineNumbers
operator|=
name|toBoolean
argument_list|(
name|rs
operator|.
name|getString
argument_list|(
literal|"hide_line_numbers"
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|availableColumns
operator|.
name|contains
argument_list|(
literal|"hide_top_menu"
argument_list|)
condition|)
block|{
name|prefs
operator|.
name|hideTopMenu
operator|=
name|toBoolean
argument_list|(
name|rs
operator|.
name|getString
argument_list|(
literal|"hide_top_menu"
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|availableColumns
operator|.
name|contains
argument_list|(
literal|"ignore_whitespace"
argument_list|)
condition|)
block|{
comment|// Enum with char as value
name|prefs
operator|.
name|ignoreWhitespace
operator|=
name|toWhitespace
argument_list|(
name|rs
operator|.
name|getString
argument_list|(
literal|"ignore_whitespace"
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|availableColumns
operator|.
name|contains
argument_list|(
literal|"intraline_difference"
argument_list|)
condition|)
block|{
name|prefs
operator|.
name|intralineDifference
operator|=
name|toBoolean
argument_list|(
name|rs
operator|.
name|getString
argument_list|(
literal|"intraline_difference"
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|availableColumns
operator|.
name|contains
argument_list|(
literal|"line_length"
argument_list|)
condition|)
block|{
name|prefs
operator|.
name|lineLength
operator|=
name|rs
operator|.
name|getInt
argument_list|(
literal|"line_length"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|availableColumns
operator|.
name|contains
argument_list|(
literal|"manual_review"
argument_list|)
condition|)
block|{
name|prefs
operator|.
name|manualReview
operator|=
name|toBoolean
argument_list|(
name|rs
operator|.
name|getString
argument_list|(
literal|"manual_review"
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|availableColumns
operator|.
name|contains
argument_list|(
literal|"render_entire_file"
argument_list|)
condition|)
block|{
name|prefs
operator|.
name|renderEntireFile
operator|=
name|toBoolean
argument_list|(
name|rs
operator|.
name|getString
argument_list|(
literal|"render_entire_file"
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|availableColumns
operator|.
name|contains
argument_list|(
literal|"retain_header"
argument_list|)
condition|)
block|{
name|prefs
operator|.
name|retainHeader
operator|=
name|toBoolean
argument_list|(
name|rs
operator|.
name|getString
argument_list|(
literal|"retain_header"
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|availableColumns
operator|.
name|contains
argument_list|(
literal|"show_line_endings"
argument_list|)
condition|)
block|{
name|prefs
operator|.
name|showLineEndings
operator|=
name|toBoolean
argument_list|(
name|rs
operator|.
name|getString
argument_list|(
literal|"show_line_endings"
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|availableColumns
operator|.
name|contains
argument_list|(
literal|"show_tabs"
argument_list|)
condition|)
block|{
name|prefs
operator|.
name|showTabs
operator|=
name|toBoolean
argument_list|(
name|rs
operator|.
name|getString
argument_list|(
literal|"show_tabs"
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|availableColumns
operator|.
name|contains
argument_list|(
literal|"show_whitespace_errors"
argument_list|)
condition|)
block|{
name|prefs
operator|.
name|showWhitespaceErrors
operator|=
name|toBoolean
argument_list|(
name|rs
operator|.
name|getString
argument_list|(
literal|"show_whitespace_errors"
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|availableColumns
operator|.
name|contains
argument_list|(
literal|"skip_deleted"
argument_list|)
condition|)
block|{
name|prefs
operator|.
name|skipDeleted
operator|=
name|toBoolean
argument_list|(
name|rs
operator|.
name|getString
argument_list|(
literal|"skip_deleted"
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|availableColumns
operator|.
name|contains
argument_list|(
literal|"skip_uncommented"
argument_list|)
condition|)
block|{
name|prefs
operator|.
name|skipUncommented
operator|=
name|toBoolean
argument_list|(
name|rs
operator|.
name|getString
argument_list|(
literal|"skip_uncommented"
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|availableColumns
operator|.
name|contains
argument_list|(
literal|"syntax_highlighting"
argument_list|)
condition|)
block|{
name|prefs
operator|.
name|syntaxHighlighting
operator|=
name|toBoolean
argument_list|(
name|rs
operator|.
name|getString
argument_list|(
literal|"syntax_highlighting"
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|availableColumns
operator|.
name|contains
argument_list|(
literal|"tab_size"
argument_list|)
condition|)
block|{
name|prefs
operator|.
name|tabSize
operator|=
name|rs
operator|.
name|getInt
argument_list|(
literal|"tab_size"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|availableColumns
operator|.
name|contains
argument_list|(
literal|"theme"
argument_list|)
condition|)
block|{
comment|// Enum with name as values; can be null
name|prefs
operator|.
name|theme
operator|=
name|toTheme
argument_list|(
name|rs
operator|.
name|getString
argument_list|(
literal|"theme"
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|availableColumns
operator|.
name|contains
argument_list|(
literal|"hide_empty_pane"
argument_list|)
condition|)
block|{
name|prefs
operator|.
name|hideEmptyPane
operator|=
name|toBoolean
argument_list|(
name|rs
operator|.
name|getString
argument_list|(
literal|"hide_empty_pane"
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|availableColumns
operator|.
name|contains
argument_list|(
literal|"auto_hide_diff_table_header"
argument_list|)
condition|)
block|{
name|prefs
operator|.
name|autoHideDiffTableHeader
operator|=
name|toBoolean
argument_list|(
name|rs
operator|.
name|getString
argument_list|(
literal|"auto_hide_diff_table_header"
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|imports
operator|.
name|put
argument_list|(
name|accountId
argument_list|,
name|prefs
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|imports
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return;
block|}
try|try
init|(
name|Repository
name|git
init|=
name|mgr
operator|.
name|openRepository
argument_list|(
name|allUsersName
argument_list|)
init|;
name|RevWalk
name|rw
operator|=
operator|new
name|RevWalk
argument_list|(
name|git
argument_list|)
init|)
block|{
name|BatchRefUpdate
name|bru
init|=
name|git
operator|.
name|getRefDatabase
argument_list|()
operator|.
name|newBatchUpdate
argument_list|()
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|Account
operator|.
name|Id
argument_list|,
name|DiffPreferencesInfo
argument_list|>
name|e
range|:
name|imports
operator|.
name|entrySet
argument_list|()
control|)
block|{
try|try
init|(
name|MetaDataUpdate
name|md
init|=
operator|new
name|MetaDataUpdate
argument_list|(
name|GitReferenceUpdated
operator|.
name|DISABLED
argument_list|,
name|allUsersName
argument_list|,
name|git
argument_list|,
name|bru
argument_list|)
init|)
block|{
name|md
operator|.
name|getCommitBuilder
argument_list|()
operator|.
name|setAuthor
argument_list|(
name|serverUser
argument_list|)
expr_stmt|;
name|md
operator|.
name|getCommitBuilder
argument_list|()
operator|.
name|setCommitter
argument_list|(
name|serverUser
argument_list|)
expr_stmt|;
name|VersionedAccountPreferences
name|p
init|=
name|VersionedAccountPreferences
operator|.
name|forUser
argument_list|(
name|e
operator|.
name|getKey
argument_list|()
argument_list|)
decl_stmt|;
name|p
operator|.
name|load
argument_list|(
name|md
argument_list|)
expr_stmt|;
name|storeSection
argument_list|(
name|p
operator|.
name|getConfig
argument_list|()
argument_list|,
name|UserConfigSections
operator|.
name|DIFF
argument_list|,
literal|null
argument_list|,
name|e
operator|.
name|getValue
argument_list|()
argument_list|,
name|DiffPreferencesInfo
operator|.
name|defaults
argument_list|()
argument_list|)
expr_stmt|;
name|p
operator|.
name|commit
argument_list|(
name|md
argument_list|)
expr_stmt|;
block|}
block|}
name|bru
operator|.
name|execute
argument_list|(
name|rw
argument_list|,
name|NullProgressMonitor
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ConfigInvalidException
decl||
name|IOException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|OrmException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
block|}
DECL|method|getColumns (ResultSet rs)
specifier|private
name|Set
argument_list|<
name|String
argument_list|>
name|getColumns
parameter_list|(
name|ResultSet
name|rs
parameter_list|)
throws|throws
name|SQLException
block|{
name|ResultSetMetaData
name|metaData
init|=
name|rs
operator|.
name|getMetaData
argument_list|()
decl_stmt|;
name|int
name|columnCount
init|=
name|metaData
operator|.
name|getColumnCount
argument_list|()
decl_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|columns
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|(
name|columnCount
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|i
operator|<=
name|columnCount
condition|;
name|i
operator|++
control|)
block|{
name|columns
operator|.
name|add
argument_list|(
name|metaData
operator|.
name|getColumnLabel
argument_list|(
name|i
argument_list|)
operator|.
name|toLowerCase
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|columns
return|;
block|}
DECL|method|toTheme (String v)
specifier|private
specifier|static
name|Theme
name|toTheme
parameter_list|(
name|String
name|v
parameter_list|)
block|{
if|if
condition|(
name|v
operator|==
literal|null
condition|)
block|{
return|return
name|Theme
operator|.
name|DEFAULT
return|;
block|}
return|return
name|Theme
operator|.
name|valueOf
argument_list|(
name|v
argument_list|)
return|;
block|}
DECL|method|toWhitespace (String v)
specifier|private
specifier|static
name|Whitespace
name|toWhitespace
parameter_list|(
name|String
name|v
parameter_list|)
block|{
name|requireNonNull
argument_list|(
name|v
argument_list|)
expr_stmt|;
if|if
condition|(
name|v
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|Whitespace
operator|.
name|IGNORE_NONE
return|;
block|}
name|Whitespace
name|r
init|=
name|PatchListKey
operator|.
name|WHITESPACE_TYPES
operator|.
name|inverse
argument_list|()
operator|.
name|get
argument_list|(
name|v
operator|.
name|charAt
argument_list|(
literal|0
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|r
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Cannot find Whitespace type for: "
operator|+
name|v
argument_list|)
throw|;
block|}
return|return
name|r
return|;
block|}
DECL|method|toBoolean (String v)
specifier|private
specifier|static
name|boolean
name|toBoolean
parameter_list|(
name|String
name|v
parameter_list|)
block|{
name|checkState
argument_list|(
operator|!
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|v
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|v
operator|.
name|equals
argument_list|(
literal|"Y"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

