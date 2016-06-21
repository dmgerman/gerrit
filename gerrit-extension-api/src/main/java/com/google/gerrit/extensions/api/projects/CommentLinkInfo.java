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
DECL|package|com.google.gerrit.extensions.api.projects
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|extensions
operator|.
name|api
operator|.
name|projects
package|;
end_package

begin_class
DECL|class|CommentLinkInfo
specifier|public
class|class
name|CommentLinkInfo
block|{
DECL|field|match
specifier|public
name|String
name|match
decl_stmt|;
DECL|field|link
specifier|public
name|String
name|link
decl_stmt|;
DECL|field|html
specifier|public
name|String
name|html
decl_stmt|;
DECL|field|enabled
specifier|public
name|Boolean
name|enabled
decl_stmt|;
comment|// null means true
DECL|field|name
specifier|public
specifier|transient
name|String
name|name
decl_stmt|;
block|}
end_class

end_unit

