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
DECL|package|com.google.gerrit.extensions.common
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|extensions
operator|.
name|common
package|;
end_package

begin_class
DECL|class|AvatarInfo
specifier|public
class|class
name|AvatarInfo
block|{
comment|/**    * Size in pixels the UI prefers an avatar image to be.    *    *<p>The web UI prefers avatar images to be square, both the height and width of the image should    * be this size. The height is the more important dimension to match than the width.    */
DECL|field|DEFAULT_SIZE
specifier|public
specifier|static
specifier|final
name|int
name|DEFAULT_SIZE
init|=
literal|32
decl_stmt|;
DECL|field|url
specifier|public
name|String
name|url
decl_stmt|;
DECL|field|height
specifier|public
name|Integer
name|height
decl_stmt|;
DECL|field|width
specifier|public
name|Integer
name|width
decl_stmt|;
block|}
end_class

end_unit

