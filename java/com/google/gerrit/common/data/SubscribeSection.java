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
DECL|package|com.google.gerrit.common.data
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|common
operator|.
name|data
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
name|entities
operator|.
name|BranchNameKey
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
name|entities
operator|.
name|Project
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
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
name|List
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
name|transport
operator|.
name|RefSpec
import|;
end_import

begin_comment
comment|/** Portion of a {@link Project} describing superproject subscription rules. */
end_comment

begin_class
DECL|class|SubscribeSection
specifier|public
class|class
name|SubscribeSection
block|{
DECL|field|multiMatchRefSpecs
specifier|private
specifier|final
name|List
argument_list|<
name|RefSpec
argument_list|>
name|multiMatchRefSpecs
decl_stmt|;
DECL|field|matchingRefSpecs
specifier|private
specifier|final
name|List
argument_list|<
name|RefSpec
argument_list|>
name|matchingRefSpecs
decl_stmt|;
DECL|field|project
specifier|private
specifier|final
name|Project
operator|.
name|NameKey
name|project
decl_stmt|;
DECL|method|SubscribeSection (Project.NameKey p)
specifier|public
name|SubscribeSection
parameter_list|(
name|Project
operator|.
name|NameKey
name|p
parameter_list|)
block|{
name|project
operator|=
name|p
expr_stmt|;
name|matchingRefSpecs
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
name|multiMatchRefSpecs
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
block|}
DECL|method|addMatchingRefSpec (RefSpec spec)
specifier|public
name|void
name|addMatchingRefSpec
parameter_list|(
name|RefSpec
name|spec
parameter_list|)
block|{
name|matchingRefSpecs
operator|.
name|add
argument_list|(
name|spec
argument_list|)
expr_stmt|;
block|}
DECL|method|addMatchingRefSpec (String spec)
specifier|public
name|void
name|addMatchingRefSpec
parameter_list|(
name|String
name|spec
parameter_list|)
block|{
name|RefSpec
name|r
init|=
operator|new
name|RefSpec
argument_list|(
name|spec
argument_list|)
decl_stmt|;
name|matchingRefSpecs
operator|.
name|add
argument_list|(
name|r
argument_list|)
expr_stmt|;
block|}
DECL|method|addMultiMatchRefSpec (String spec)
specifier|public
name|void
name|addMultiMatchRefSpec
parameter_list|(
name|String
name|spec
parameter_list|)
block|{
name|RefSpec
name|r
init|=
operator|new
name|RefSpec
argument_list|(
name|spec
argument_list|,
name|RefSpec
operator|.
name|WildcardMode
operator|.
name|ALLOW_MISMATCH
argument_list|)
decl_stmt|;
name|multiMatchRefSpecs
operator|.
name|add
argument_list|(
name|r
argument_list|)
expr_stmt|;
block|}
DECL|method|getProject ()
specifier|public
name|Project
operator|.
name|NameKey
name|getProject
parameter_list|()
block|{
return|return
name|project
return|;
block|}
comment|/**    * Determines if the<code>branch</code> could trigger a superproject update as allowed via this    * subscribe section.    *    * @param branch the branch to check    * @return if the branch could trigger a superproject update    */
DECL|method|appliesTo (BranchNameKey branch)
specifier|public
name|boolean
name|appliesTo
parameter_list|(
name|BranchNameKey
name|branch
parameter_list|)
block|{
for|for
control|(
name|RefSpec
name|r
range|:
name|matchingRefSpecs
control|)
block|{
if|if
condition|(
name|r
operator|.
name|matchSource
argument_list|(
name|branch
operator|.
name|branch
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
for|for
control|(
name|RefSpec
name|r
range|:
name|multiMatchRefSpecs
control|)
block|{
if|if
condition|(
name|r
operator|.
name|matchSource
argument_list|(
name|branch
operator|.
name|branch
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
DECL|method|getMatchingRefSpecs ()
specifier|public
name|Collection
argument_list|<
name|RefSpec
argument_list|>
name|getMatchingRefSpecs
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|unmodifiableCollection
argument_list|(
name|matchingRefSpecs
argument_list|)
return|;
block|}
DECL|method|getMultiMatchRefSpecs ()
specifier|public
name|Collection
argument_list|<
name|RefSpec
argument_list|>
name|getMultiMatchRefSpecs
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|unmodifiableCollection
argument_list|(
name|multiMatchRefSpecs
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
name|StringBuilder
name|ret
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|ret
operator|.
name|append
argument_list|(
literal|"[SubscribeSection, project="
argument_list|)
expr_stmt|;
name|ret
operator|.
name|append
argument_list|(
name|project
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|matchingRefSpecs
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|ret
operator|.
name|append
argument_list|(
literal|", matching=["
argument_list|)
expr_stmt|;
for|for
control|(
name|RefSpec
name|r
range|:
name|matchingRefSpecs
control|)
block|{
name|ret
operator|.
name|append
argument_list|(
name|r
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|ret
operator|.
name|append
argument_list|(
literal|", "
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
operator|!
name|multiMatchRefSpecs
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|ret
operator|.
name|append
argument_list|(
literal|", all=["
argument_list|)
expr_stmt|;
for|for
control|(
name|RefSpec
name|r
range|:
name|multiMatchRefSpecs
control|)
block|{
name|ret
operator|.
name|append
argument_list|(
name|r
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|ret
operator|.
name|append
argument_list|(
literal|", "
argument_list|)
expr_stmt|;
block|}
block|}
name|ret
operator|.
name|append
argument_list|(
literal|"]"
argument_list|)
expr_stmt|;
return|return
name|ret
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

