begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2011 The Android Open Source Project
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
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|reviewdb
operator|.
name|Project
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
name|reviewdb
operator|.
name|SystemConfig
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
name|LocalDiskRepositoryManager
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
name|client
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
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|Constants
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
name|RepositoryCache
operator|.
name|FileKey
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
name|util
operator|.
name|FS
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_class
DECL|class|Schema_55
specifier|public
class|class
name|Schema_55
extends|extends
name|SchemaVersion
block|{
DECL|field|mgr
specifier|private
specifier|final
name|LocalDiskRepositoryManager
name|mgr
decl_stmt|;
annotation|@
name|Inject
DECL|method|Schema_55 (Provider<Schema_54> prior, LocalDiskRepositoryManager mgr)
name|Schema_55
parameter_list|(
name|Provider
argument_list|<
name|Schema_54
argument_list|>
name|prior
parameter_list|,
name|LocalDiskRepositoryManager
name|mgr
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
block|{
name|SystemConfig
name|sc
init|=
name|db
operator|.
name|systemConfig
argument_list|()
operator|.
name|get
argument_list|(
operator|new
name|SystemConfig
operator|.
name|Key
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|oldName
init|=
name|sc
operator|.
name|wildProjectName
operator|.
name|get
argument_list|()
decl_stmt|;
name|String
name|newName
init|=
literal|"All-Projects"
decl_stmt|;
if|if
condition|(
literal|"-- All Projects --"
operator|.
name|equals
argument_list|(
name|oldName
argument_list|)
condition|)
block|{
name|ui
operator|.
name|message
argument_list|(
literal|"Renaming \""
operator|+
name|oldName
operator|+
literal|"\" to \""
operator|+
name|newName
operator|+
literal|"\""
argument_list|)
expr_stmt|;
name|File
name|base
init|=
name|mgr
operator|.
name|getBasePath
argument_list|()
decl_stmt|;
name|File
name|oldDir
init|=
name|FileKey
operator|.
name|resolve
argument_list|(
operator|new
name|File
argument_list|(
name|base
argument_list|,
name|oldName
argument_list|)
argument_list|,
name|FS
operator|.
name|DETECTED
argument_list|)
decl_stmt|;
name|File
name|newDir
init|=
operator|new
name|File
argument_list|(
name|base
argument_list|,
name|newName
operator|+
name|Constants
operator|.
name|DOT_GIT_EXT
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|oldDir
operator|.
name|renameTo
argument_list|(
name|newDir
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|OrmException
argument_list|(
literal|"Cannot rename "
operator|+
name|oldDir
operator|.
name|getAbsolutePath
argument_list|()
operator|+
literal|" to "
operator|+
name|newDir
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
throw|;
block|}
name|sc
operator|.
name|wildProjectName
operator|=
operator|new
name|Project
operator|.
name|NameKey
argument_list|(
name|newName
argument_list|)
expr_stmt|;
name|db
operator|.
name|systemConfig
argument_list|()
operator|.
name|update
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
name|sc
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

