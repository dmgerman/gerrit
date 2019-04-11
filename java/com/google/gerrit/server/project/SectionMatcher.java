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
name|common
operator|.
name|data
operator|.
name|AccessSection
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

begin_comment
comment|/**  * Matches an AccessSection against a reference name.  *  *<p>These matchers are "compiled" versions of the AccessSection name, supporting faster selection  * of which sections are relevant to any given input reference.  */
end_comment

begin_class
DECL|class|SectionMatcher
specifier|public
class|class
name|SectionMatcher
extends|extends
name|RefPatternMatcher
block|{
DECL|method|wrap (Project.NameKey project, AccessSection section)
specifier|static
name|SectionMatcher
name|wrap
parameter_list|(
name|Project
operator|.
name|NameKey
name|project
parameter_list|,
name|AccessSection
name|section
parameter_list|)
block|{
name|String
name|ref
init|=
name|section
operator|.
name|getName
argument_list|()
decl_stmt|;
if|if
condition|(
name|AccessSection
operator|.
name|isValidRefSectionName
argument_list|(
name|ref
argument_list|)
condition|)
block|{
return|return
operator|new
name|SectionMatcher
argument_list|(
name|project
argument_list|,
name|section
argument_list|,
name|getMatcher
argument_list|(
name|ref
argument_list|)
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
DECL|field|project
specifier|private
specifier|final
name|Project
operator|.
name|NameKey
name|project
decl_stmt|;
DECL|field|section
specifier|private
specifier|final
name|AccessSection
name|section
decl_stmt|;
DECL|field|matcher
specifier|private
specifier|final
name|RefPatternMatcher
name|matcher
decl_stmt|;
DECL|method|SectionMatcher (Project.NameKey project, AccessSection section, RefPatternMatcher matcher)
specifier|public
name|SectionMatcher
parameter_list|(
name|Project
operator|.
name|NameKey
name|project
parameter_list|,
name|AccessSection
name|section
parameter_list|,
name|RefPatternMatcher
name|matcher
parameter_list|)
block|{
name|this
operator|.
name|project
operator|=
name|project
expr_stmt|;
name|this
operator|.
name|section
operator|=
name|section
expr_stmt|;
name|this
operator|.
name|matcher
operator|=
name|matcher
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|match (String ref, CurrentUser user)
specifier|public
name|boolean
name|match
parameter_list|(
name|String
name|ref
parameter_list|,
name|CurrentUser
name|user
parameter_list|)
block|{
return|return
name|this
operator|.
name|matcher
operator|.
name|match
argument_list|(
name|ref
argument_list|,
name|user
argument_list|)
return|;
block|}
DECL|method|getSection ()
specifier|public
name|AccessSection
name|getSection
parameter_list|()
block|{
return|return
name|section
return|;
block|}
DECL|method|getMatcher ()
specifier|public
name|RefPatternMatcher
name|getMatcher
parameter_list|()
block|{
return|return
name|matcher
return|;
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
block|}
end_class

end_unit

