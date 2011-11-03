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
DECL|package|com.google.gerrit.server.project
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|project
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
name|server
operator|.
name|CurrentUser
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
name|servlet
operator|.
name|RequestScoped
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

begin_comment
comment|/** Caches {@link ProjectControl} objects for the current user of the request. */
end_comment

begin_class
annotation|@
name|RequestScoped
DECL|class|PerRequestProjectControlCache
specifier|public
class|class
name|PerRequestProjectControlCache
block|{
DECL|field|projectCache
specifier|private
specifier|final
name|ProjectCache
name|projectCache
decl_stmt|;
DECL|field|user
specifier|private
specifier|final
name|CurrentUser
name|user
decl_stmt|;
DECL|field|controls
specifier|private
specifier|final
name|Map
argument_list|<
name|Project
operator|.
name|NameKey
argument_list|,
name|ProjectControl
argument_list|>
name|controls
decl_stmt|;
annotation|@
name|Inject
DECL|method|PerRequestProjectControlCache (ProjectCache projectCache, CurrentUser userProvider)
name|PerRequestProjectControlCache
parameter_list|(
name|ProjectCache
name|projectCache
parameter_list|,
name|CurrentUser
name|userProvider
parameter_list|)
block|{
name|this
operator|.
name|projectCache
operator|=
name|projectCache
expr_stmt|;
name|this
operator|.
name|user
operator|=
name|userProvider
expr_stmt|;
name|this
operator|.
name|controls
operator|=
operator|new
name|HashMap
argument_list|<
name|Project
operator|.
name|NameKey
argument_list|,
name|ProjectControl
argument_list|>
argument_list|()
expr_stmt|;
block|}
DECL|method|get (Project.NameKey nameKey)
name|ProjectControl
name|get
parameter_list|(
name|Project
operator|.
name|NameKey
name|nameKey
parameter_list|)
throws|throws
name|NoSuchProjectException
block|{
name|ProjectControl
name|ctl
init|=
name|controls
operator|.
name|get
argument_list|(
name|nameKey
argument_list|)
decl_stmt|;
if|if
condition|(
name|ctl
operator|==
literal|null
condition|)
block|{
name|ProjectState
name|p
init|=
name|projectCache
operator|.
name|get
argument_list|(
name|nameKey
argument_list|)
decl_stmt|;
if|if
condition|(
name|p
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NoSuchProjectException
argument_list|(
name|nameKey
argument_list|)
throw|;
block|}
name|ctl
operator|=
name|p
operator|.
name|controlFor
argument_list|(
name|user
argument_list|)
expr_stmt|;
name|controls
operator|.
name|put
argument_list|(
name|nameKey
argument_list|,
name|ctl
argument_list|)
expr_stmt|;
block|}
return|return
name|ctl
return|;
block|}
DECL|method|evict (Project project)
specifier|public
name|void
name|evict
parameter_list|(
name|Project
name|project
parameter_list|)
block|{
name|projectCache
operator|.
name|evict
argument_list|(
name|project
argument_list|)
expr_stmt|;
name|controls
operator|.
name|remove
argument_list|(
name|project
operator|.
name|getNameKey
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

