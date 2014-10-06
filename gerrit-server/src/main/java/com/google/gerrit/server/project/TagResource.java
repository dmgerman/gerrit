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
name|extensions
operator|.
name|common
operator|.
name|TagInfo
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
name|inject
operator|.
name|TypeLiteral
import|;
end_import

begin_class
DECL|class|TagResource
specifier|public
class|class
name|TagResource
extends|extends
name|ProjectResource
block|{
DECL|field|TAG_KIND
specifier|public
specifier|static
specifier|final
name|TypeLiteral
argument_list|<
name|RestView
argument_list|<
name|TagResource
argument_list|>
argument_list|>
name|TAG_KIND
init|=
operator|new
name|TypeLiteral
argument_list|<
name|RestView
argument_list|<
name|TagResource
argument_list|>
argument_list|>
argument_list|()
block|{}
decl_stmt|;
DECL|field|tag
specifier|private
specifier|final
name|TagInfo
name|tag
decl_stmt|;
DECL|method|TagResource (ProjectControl control, TagInfo tag)
specifier|public
name|TagResource
parameter_list|(
name|ProjectControl
name|control
parameter_list|,
name|TagInfo
name|tag
parameter_list|)
block|{
name|super
argument_list|(
name|control
argument_list|)
expr_stmt|;
name|this
operator|.
name|tag
operator|=
name|tag
expr_stmt|;
block|}
DECL|method|getTagInfo ()
specifier|public
name|TagInfo
name|getTagInfo
parameter_list|()
block|{
return|return
name|tag
return|;
block|}
block|}
end_class

end_unit

