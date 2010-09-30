begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2009 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.git
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|git
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
name|Project
operator|.
name|SubmitType
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
name|gwtorm
operator|.
name|client
operator|.
name|SchemaFactory
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
name|Collections
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
name|Set
import|;
end_import

begin_comment
comment|/** Imports all projects found within the repository manager. */
end_comment

begin_class
DECL|class|GitProjectImporter
specifier|public
class|class
name|GitProjectImporter
block|{
DECL|interface|Messages
specifier|public
interface|interface
name|Messages
block|{
DECL|method|info (String msg)
name|void
name|info
parameter_list|(
name|String
name|msg
parameter_list|)
function_decl|;
DECL|method|warning (String msg)
name|void
name|warning
parameter_list|(
name|String
name|msg
parameter_list|)
function_decl|;
block|}
DECL|field|repositoryManager
specifier|private
specifier|final
name|LocalDiskRepositoryManager
name|repositoryManager
decl_stmt|;
DECL|field|schema
specifier|private
specifier|final
name|SchemaFactory
argument_list|<
name|ReviewDb
argument_list|>
name|schema
decl_stmt|;
DECL|field|messages
specifier|private
name|Messages
name|messages
decl_stmt|;
annotation|@
name|Inject
DECL|method|GitProjectImporter (final LocalDiskRepositoryManager repositoryManager, final SchemaFactory<ReviewDb> schema)
name|GitProjectImporter
parameter_list|(
specifier|final
name|LocalDiskRepositoryManager
name|repositoryManager
parameter_list|,
specifier|final
name|SchemaFactory
argument_list|<
name|ReviewDb
argument_list|>
name|schema
parameter_list|)
block|{
name|this
operator|.
name|repositoryManager
operator|=
name|repositoryManager
expr_stmt|;
name|this
operator|.
name|schema
operator|=
name|schema
expr_stmt|;
block|}
DECL|method|run (final Messages msg)
specifier|public
name|void
name|run
parameter_list|(
specifier|final
name|Messages
name|msg
parameter_list|)
throws|throws
name|OrmException
throws|,
name|IOException
block|{
name|messages
operator|=
name|msg
expr_stmt|;
name|messages
operator|.
name|info
argument_list|(
literal|"Scanning "
operator|+
name|repositoryManager
operator|.
name|getBasePath
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|ReviewDb
name|db
init|=
name|schema
operator|.
name|open
argument_list|()
decl_stmt|;
try|try
block|{
specifier|final
name|HashSet
argument_list|<
name|String
argument_list|>
name|have
init|=
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|Project
name|p
range|:
name|db
operator|.
name|projects
argument_list|()
operator|.
name|all
argument_list|()
control|)
block|{
name|have
operator|.
name|add
argument_list|(
name|p
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|importProjects
argument_list|(
name|repositoryManager
operator|.
name|getBasePath
argument_list|()
argument_list|,
literal|""
argument_list|,
name|db
argument_list|,
name|have
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|db
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|importProjects (final File dir, final String prefix, final ReviewDb db, final Set<String> have)
specifier|private
name|void
name|importProjects
parameter_list|(
specifier|final
name|File
name|dir
parameter_list|,
specifier|final
name|String
name|prefix
parameter_list|,
specifier|final
name|ReviewDb
name|db
parameter_list|,
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|have
parameter_list|)
throws|throws
name|OrmException
throws|,
name|IOException
block|{
specifier|final
name|File
index|[]
name|ls
init|=
name|dir
operator|.
name|listFiles
argument_list|()
decl_stmt|;
if|if
condition|(
name|ls
operator|==
literal|null
condition|)
block|{
return|return;
block|}
for|for
control|(
name|File
name|f
range|:
name|ls
control|)
block|{
name|String
name|name
init|=
name|f
operator|.
name|getName
argument_list|()
decl_stmt|;
if|if
condition|(
literal|"."
operator|.
name|equals
argument_list|(
name|name
argument_list|)
operator|||
literal|".."
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
continue|continue;
block|}
if|if
condition|(
name|FileKey
operator|.
name|isGitRepository
argument_list|(
name|f
argument_list|,
name|FS
operator|.
name|DETECTED
argument_list|)
condition|)
block|{
if|if
condition|(
name|name
operator|.
name|equals
argument_list|(
literal|".git"
argument_list|)
condition|)
block|{
name|name
operator|=
name|prefix
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|prefix
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|name
operator|.
name|endsWith
argument_list|(
literal|".git"
argument_list|)
condition|)
block|{
name|name
operator|=
name|prefix
operator|+
name|name
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|name
operator|.
name|length
argument_list|()
operator|-
literal|4
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|name
operator|=
name|prefix
operator|+
name|name
expr_stmt|;
if|if
condition|(
operator|!
name|have
operator|.
name|contains
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|messages
operator|.
name|warning
argument_list|(
literal|"Importing non-standard name '"
operator|+
name|name
operator|+
literal|"'"
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|have
operator|.
name|contains
argument_list|(
name|name
argument_list|)
condition|)
block|{
continue|continue;
block|}
specifier|final
name|Project
operator|.
name|NameKey
name|nameKey
init|=
operator|new
name|Project
operator|.
name|NameKey
argument_list|(
name|name
argument_list|)
decl_stmt|;
specifier|final
name|Project
name|p
init|=
operator|new
name|Project
argument_list|(
name|nameKey
argument_list|)
decl_stmt|;
name|p
operator|.
name|setDescription
argument_list|(
name|repositoryManager
operator|.
name|getProjectDescription
argument_list|(
name|name
argument_list|)
argument_list|)
expr_stmt|;
name|p
operator|.
name|setSubmitType
argument_list|(
name|SubmitType
operator|.
name|MERGE_IF_NECESSARY
argument_list|)
expr_stmt|;
name|p
operator|.
name|setUseContributorAgreements
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|p
operator|.
name|setUseSignedOffBy
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|p
operator|.
name|setUseContentMerge
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|p
operator|.
name|setRequireChangeID
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|db
operator|.
name|projects
argument_list|()
operator|.
name|insert
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
name|p
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|f
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
name|importProjects
argument_list|(
name|f
argument_list|,
name|prefix
operator|+
name|f
operator|.
name|getName
argument_list|()
operator|+
literal|"/"
argument_list|,
name|db
argument_list|,
name|have
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

