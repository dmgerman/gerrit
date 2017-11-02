begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2015 The Android Open Source Project
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
DECL|package|com.google.gerrit.extensions.client
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|extensions
operator|.
name|client
package|;
end_package

begin_class
DECL|class|MenuItem
specifier|public
class|class
name|MenuItem
block|{
DECL|field|url
specifier|public
specifier|final
name|String
name|url
decl_stmt|;
DECL|field|name
specifier|public
specifier|final
name|String
name|name
decl_stmt|;
DECL|field|target
specifier|public
specifier|final
name|String
name|target
decl_stmt|;
DECL|field|id
specifier|public
specifier|final
name|String
name|id
decl_stmt|;
comment|// Needed for GWT
DECL|method|MenuItem ()
specifier|public
name|MenuItem
parameter_list|()
block|{
name|this
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|MenuItem (String name, String url)
specifier|public
name|MenuItem
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|url
parameter_list|)
block|{
name|this
argument_list|(
name|name
argument_list|,
name|url
argument_list|,
literal|"_blank"
argument_list|)
expr_stmt|;
block|}
DECL|method|MenuItem (String name, String url, String target)
specifier|public
name|MenuItem
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|url
parameter_list|,
name|String
name|target
parameter_list|)
block|{
name|this
argument_list|(
name|name
argument_list|,
name|url
argument_list|,
name|target
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|MenuItem (String name, String url, String target, String id)
specifier|public
name|MenuItem
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|url
parameter_list|,
name|String
name|target
parameter_list|,
name|String
name|id
parameter_list|)
block|{
name|this
operator|.
name|url
operator|=
name|url
expr_stmt|;
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
name|this
operator|.
name|target
operator|=
name|target
expr_stmt|;
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
block|}
block|}
end_class

end_unit
