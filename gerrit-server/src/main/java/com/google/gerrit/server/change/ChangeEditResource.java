begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2014 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.change
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|change
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
name|extensions
operator|.
name|restapi
operator|.
name|RestResource
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
name|restapi
operator|.
name|RestView
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
name|edit
operator|.
name|ChangeEdit
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
name|TypeLiteral
import|;
end_import

begin_comment
comment|/**  * Represents change edit resource, that is actualy two kinds of resources:  *  *<ul>  *<li>the change edit itself  *<li>a path within the edit  *</ul>  *  * distinguished by whether path is null or not.  */
end_comment

begin_class
DECL|class|ChangeEditResource
specifier|public
class|class
name|ChangeEditResource
implements|implements
name|RestResource
block|{
DECL|field|CHANGE_EDIT_KIND
specifier|public
specifier|static
specifier|final
name|TypeLiteral
argument_list|<
name|RestView
argument_list|<
name|ChangeEditResource
argument_list|>
argument_list|>
name|CHANGE_EDIT_KIND
init|=
operator|new
name|TypeLiteral
argument_list|<
name|RestView
argument_list|<
name|ChangeEditResource
argument_list|>
argument_list|>
argument_list|()
block|{}
decl_stmt|;
DECL|field|change
specifier|private
specifier|final
name|ChangeResource
name|change
decl_stmt|;
DECL|field|edit
specifier|private
specifier|final
name|ChangeEdit
name|edit
decl_stmt|;
DECL|field|path
specifier|private
specifier|final
name|String
name|path
decl_stmt|;
DECL|method|ChangeEditResource (ChangeResource change, ChangeEdit edit, String path)
specifier|public
name|ChangeEditResource
parameter_list|(
name|ChangeResource
name|change
parameter_list|,
name|ChangeEdit
name|edit
parameter_list|,
name|String
name|path
parameter_list|)
block|{
name|this
operator|.
name|change
operator|=
name|change
expr_stmt|;
name|this
operator|.
name|edit
operator|=
name|edit
expr_stmt|;
name|this
operator|.
name|path
operator|=
name|path
expr_stmt|;
block|}
comment|// TODO(davido): Make this cacheable.
comment|// Should just depend on the SHA-1 of the edit itself.
DECL|method|isCacheable ()
specifier|public
name|boolean
name|isCacheable
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
DECL|method|getChangeResource ()
specifier|public
name|ChangeResource
name|getChangeResource
parameter_list|()
block|{
return|return
name|change
return|;
block|}
DECL|method|getChangeEdit ()
specifier|public
name|ChangeEdit
name|getChangeEdit
parameter_list|()
block|{
return|return
name|edit
return|;
block|}
DECL|method|getPath ()
specifier|public
name|String
name|getPath
parameter_list|()
block|{
return|return
name|path
return|;
block|}
block|}
end_class

end_unit

