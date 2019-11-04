begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2019 The Android Open Source Project
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
name|java
operator|.
name|io
operator|.
name|IOException
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
name|attributes
operator|.
name|AttributesNodeProvider
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
name|BaseRepositoryBuilder
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
name|ObjectDatabase
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
name|RefDatabase
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
name|ReflogReader
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
name|lib
operator|.
name|StoredConfig
import|;
end_import

begin_comment
comment|/** Wrapper around {@link Repository} that delegates all calls to the wrapped {@link Repository}. */
end_comment

begin_class
DECL|class|DelegateRepository
class|class
name|DelegateRepository
extends|extends
name|Repository
block|{
DECL|field|delegate
specifier|private
specifier|final
name|Repository
name|delegate
decl_stmt|;
DECL|method|DelegateRepository (Repository delegate)
name|DelegateRepository
parameter_list|(
name|Repository
name|delegate
parameter_list|)
block|{
name|super
argument_list|(
name|toBuilder
argument_list|(
name|delegate
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|delegate
operator|=
name|delegate
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|create (boolean bare)
specifier|public
name|void
name|create
parameter_list|(
name|boolean
name|bare
parameter_list|)
throws|throws
name|IOException
block|{
name|delegate
operator|.
name|create
argument_list|(
name|bare
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getIdentifier ()
specifier|public
name|String
name|getIdentifier
parameter_list|()
block|{
return|return
name|delegate
operator|.
name|getIdentifier
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getObjectDatabase ()
specifier|public
name|ObjectDatabase
name|getObjectDatabase
parameter_list|()
block|{
return|return
name|delegate
operator|.
name|getObjectDatabase
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getRefDatabase ()
specifier|public
name|RefDatabase
name|getRefDatabase
parameter_list|()
block|{
return|return
name|delegate
operator|.
name|getRefDatabase
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getConfig ()
specifier|public
name|StoredConfig
name|getConfig
parameter_list|()
block|{
return|return
name|delegate
operator|.
name|getConfig
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|createAttributesNodeProvider ()
specifier|public
name|AttributesNodeProvider
name|createAttributesNodeProvider
parameter_list|()
block|{
return|return
name|delegate
operator|.
name|createAttributesNodeProvider
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|scanForRepoChanges ()
specifier|public
name|void
name|scanForRepoChanges
parameter_list|()
throws|throws
name|IOException
block|{
name|delegate
operator|.
name|scanForRepoChanges
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|notifyIndexChanged (boolean internal)
specifier|public
name|void
name|notifyIndexChanged
parameter_list|(
name|boolean
name|internal
parameter_list|)
block|{
name|delegate
operator|.
name|notifyIndexChanged
argument_list|(
name|internal
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getReflogReader (String refName)
specifier|public
name|ReflogReader
name|getReflogReader
parameter_list|(
name|String
name|refName
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|delegate
operator|.
name|getReflogReader
argument_list|(
name|refName
argument_list|)
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"rawtypes"
argument_list|)
DECL|method|toBuilder (Repository repo)
specifier|private
specifier|static
name|BaseRepositoryBuilder
name|toBuilder
parameter_list|(
name|Repository
name|repo
parameter_list|)
block|{
if|if
condition|(
operator|!
name|repo
operator|.
name|isBare
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"non-bare repository is not supported: "
operator|+
name|repo
operator|.
name|getIdentifier
argument_list|()
argument_list|)
throw|;
block|}
return|return
operator|new
name|BaseRepositoryBuilder
argument_list|<>
argument_list|()
operator|.
name|setFS
argument_list|(
name|repo
operator|.
name|getFS
argument_list|()
argument_list|)
operator|.
name|setGitDir
argument_list|(
name|repo
operator|.
name|getDirectory
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

