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
name|java
operator|.
name|util
operator|.
name|List
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
name|annotations
operator|.
name|NonNull
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
name|ObjectId
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
name|RefRename
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

begin_comment
comment|/**  * Wrapper around {@link RefDatabase} that delegates all calls to the wrapped {@link Repository}'s  * {@link RefDatabase}.  */
end_comment

begin_class
DECL|class|DelegateRefDatabase
specifier|public
class|class
name|DelegateRefDatabase
extends|extends
name|RefDatabase
block|{
DECL|field|delegate
specifier|private
name|Repository
name|delegate
decl_stmt|;
DECL|method|DelegateRefDatabase (Repository delegate)
name|DelegateRefDatabase
parameter_list|(
name|Repository
name|delegate
parameter_list|)
block|{
name|this
operator|.
name|delegate
operator|=
name|delegate
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|create ()
specifier|public
name|void
name|create
parameter_list|()
throws|throws
name|IOException
block|{
name|delegate
operator|.
name|getRefDatabase
argument_list|()
operator|.
name|create
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|close ()
specifier|public
name|void
name|close
parameter_list|()
block|{
name|delegate
operator|.
name|getRefDatabase
argument_list|()
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|isNameConflicting (String name)
specifier|public
name|boolean
name|isNameConflicting
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|delegate
operator|.
name|getRefDatabase
argument_list|()
operator|.
name|isNameConflicting
argument_list|(
name|name
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|newUpdate (String name, boolean detach)
specifier|public
name|RefUpdate
name|newUpdate
parameter_list|(
name|String
name|name
parameter_list|,
name|boolean
name|detach
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|delegate
operator|.
name|getRefDatabase
argument_list|()
operator|.
name|newUpdate
argument_list|(
name|name
argument_list|,
name|detach
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|newRename (String fromName, String toName)
specifier|public
name|RefRename
name|newRename
parameter_list|(
name|String
name|fromName
parameter_list|,
name|String
name|toName
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|delegate
operator|.
name|getRefDatabase
argument_list|()
operator|.
name|newRename
argument_list|(
name|fromName
argument_list|,
name|toName
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|exactRef (String name)
specifier|public
name|Ref
name|exactRef
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|delegate
operator|.
name|getRefDatabase
argument_list|()
operator|.
name|exactRef
argument_list|(
name|name
argument_list|)
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"deprecation"
argument_list|)
annotation|@
name|Override
DECL|method|getRefs (String prefix)
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Ref
argument_list|>
name|getRefs
parameter_list|(
name|String
name|prefix
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|delegate
operator|.
name|getRefDatabase
argument_list|()
operator|.
name|getRefs
argument_list|(
name|prefix
argument_list|)
return|;
block|}
annotation|@
name|Override
annotation|@
name|NonNull
DECL|method|getTipsWithSha1 (ObjectId id)
specifier|public
name|Set
argument_list|<
name|Ref
argument_list|>
name|getTipsWithSha1
parameter_list|(
name|ObjectId
name|id
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|delegate
operator|.
name|getRefDatabase
argument_list|()
operator|.
name|getTipsWithSha1
argument_list|(
name|id
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getAdditionalRefs ()
specifier|public
name|List
argument_list|<
name|Ref
argument_list|>
name|getAdditionalRefs
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|delegate
operator|.
name|getRefDatabase
argument_list|()
operator|.
name|getAdditionalRefs
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|peel (Ref ref)
specifier|public
name|Ref
name|peel
parameter_list|(
name|Ref
name|ref
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|delegate
operator|.
name|getRefDatabase
argument_list|()
operator|.
name|peel
argument_list|(
name|ref
argument_list|)
return|;
block|}
DECL|method|getDelegate ()
name|Repository
name|getDelegate
parameter_list|()
block|{
return|return
name|delegate
return|;
block|}
block|}
end_class

end_unit

