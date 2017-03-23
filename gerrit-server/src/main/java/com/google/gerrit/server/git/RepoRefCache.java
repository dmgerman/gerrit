begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2016 The Android Open Source Project
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
name|Collections
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
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Optional
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
name|Repository
import|;
end_import

begin_comment
comment|/** {@link RefCache} backed directly by a repository. */
end_comment

begin_class
DECL|class|RepoRefCache
specifier|public
class|class
name|RepoRefCache
implements|implements
name|RefCache
block|{
DECL|field|refdb
specifier|private
specifier|final
name|RefDatabase
name|refdb
decl_stmt|;
DECL|field|ids
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Optional
argument_list|<
name|ObjectId
argument_list|>
argument_list|>
name|ids
decl_stmt|;
DECL|method|RepoRefCache (Repository repo)
specifier|public
name|RepoRefCache
parameter_list|(
name|Repository
name|repo
parameter_list|)
block|{
name|this
operator|.
name|refdb
operator|=
name|repo
operator|.
name|getRefDatabase
argument_list|()
expr_stmt|;
name|this
operator|.
name|ids
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|get (String refName)
specifier|public
name|Optional
argument_list|<
name|ObjectId
argument_list|>
name|get
parameter_list|(
name|String
name|refName
parameter_list|)
throws|throws
name|IOException
block|{
name|Optional
argument_list|<
name|ObjectId
argument_list|>
name|id
init|=
name|ids
operator|.
name|get
argument_list|(
name|refName
argument_list|)
decl_stmt|;
if|if
condition|(
name|id
operator|!=
literal|null
condition|)
block|{
return|return
name|id
return|;
block|}
name|Ref
name|ref
init|=
name|refdb
operator|.
name|exactRef
argument_list|(
name|refName
argument_list|)
decl_stmt|;
name|id
operator|=
name|Optional
operator|.
name|ofNullable
argument_list|(
name|ref
argument_list|)
operator|.
name|map
argument_list|(
name|Ref
operator|::
name|getObjectId
argument_list|)
expr_stmt|;
name|ids
operator|.
name|put
argument_list|(
name|refName
argument_list|,
name|id
argument_list|)
expr_stmt|;
return|return
name|id
return|;
block|}
comment|/** @return an unmodifiable view of the refs that have been cached by this instance. */
DECL|method|getCachedRefs ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Optional
argument_list|<
name|ObjectId
argument_list|>
argument_list|>
name|getCachedRefs
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|unmodifiableMap
argument_list|(
name|ids
argument_list|)
return|;
block|}
block|}
end_class

end_unit

