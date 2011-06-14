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
name|LocalDiskRepositoryManager
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
name|errors
operator|.
name|RepositoryNotFoundException
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
name|Ref
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
name|RefUpdate
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

begin_class
DECL|class|Schema_56
specifier|public
class|class
name|Schema_56
extends|extends
name|SchemaVersion
block|{
DECL|field|mgr
specifier|private
specifier|final
name|LocalDiskRepositoryManager
name|mgr
decl_stmt|;
DECL|field|keysOne
specifier|private
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|keysOne
decl_stmt|;
DECL|field|keysTwo
specifier|private
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|keysTwo
decl_stmt|;
annotation|@
name|Inject
DECL|method|Schema_56 (Provider<Schema_55> prior, LocalDiskRepositoryManager mgr)
name|Schema_56
parameter_list|(
name|Provider
argument_list|<
name|Schema_55
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
name|keysOne
operator|=
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|()
expr_stmt|;
name|keysTwo
operator|=
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|()
expr_stmt|;
name|keysOne
operator|.
name|add
argument_list|(
name|GitRepositoryManager
operator|.
name|REF_CONFIG
argument_list|)
expr_stmt|;
name|keysTwo
operator|.
name|add
argument_list|(
name|GitRepositoryManager
operator|.
name|REF_CONFIG
argument_list|)
expr_stmt|;
name|keysTwo
operator|.
name|add
argument_list|(
name|Constants
operator|.
name|HEAD
argument_list|)
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
block|{
for|for
control|(
name|Project
operator|.
name|NameKey
name|name
range|:
name|mgr
operator|.
name|list
argument_list|()
control|)
block|{
name|Repository
name|git
decl_stmt|;
try|try
block|{
name|git
operator|=
name|mgr
operator|.
name|openRepository
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RepositoryNotFoundException
name|e
parameter_list|)
block|{
name|ui
operator|.
name|message
argument_list|(
literal|"warning: Cannot open "
operator|+
name|name
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
continue|continue;
block|}
try|try
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Ref
argument_list|>
name|all
init|=
name|git
operator|.
name|getAllRefs
argument_list|()
decl_stmt|;
if|if
condition|(
name|all
operator|.
name|keySet
argument_list|()
operator|.
name|equals
argument_list|(
name|keysOne
argument_list|)
operator|||
name|all
operator|.
name|keySet
argument_list|()
operator|.
name|equals
argument_list|(
name|keysTwo
argument_list|)
condition|)
block|{
try|try
block|{
name|RefUpdate
name|update
init|=
name|git
operator|.
name|updateRef
argument_list|(
name|Constants
operator|.
name|HEAD
argument_list|)
decl_stmt|;
name|update
operator|.
name|disableRefLog
argument_list|()
expr_stmt|;
name|update
operator|.
name|link
argument_list|(
name|GitRepositoryManager
operator|.
name|REF_CONFIG
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|err
parameter_list|)
block|{
name|ui
operator|.
name|message
argument_list|(
literal|"warning: "
operator|+
name|name
operator|.
name|get
argument_list|()
operator|+
literal|": Cannot update HEAD to "
operator|+
name|GitRepositoryManager
operator|.
name|REF_CONFIG
operator|+
literal|": "
operator|+
name|err
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
finally|finally
block|{
name|git
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

