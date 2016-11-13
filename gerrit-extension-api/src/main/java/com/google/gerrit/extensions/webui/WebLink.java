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
DECL|package|com.google.gerrit.extensions.webui
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|extensions
operator|.
name|webui
package|;
end_package

begin_comment
comment|/** Marks that the implementor has a method that provides a weblinkInfo */
end_comment

begin_interface
DECL|interface|WebLink
specifier|public
interface|interface
name|WebLink
block|{
comment|/** Class that holds target defaults for WebLink anchors. */
DECL|class|Target
class|class
name|Target
block|{
comment|/** Opens the link in a new window or tab */
DECL|field|BLANK
specifier|public
specifier|static
specifier|final
name|String
name|BLANK
init|=
literal|"_blank"
decl_stmt|;
comment|/** Opens the link in the frame it was clicked. */
DECL|field|SELF
specifier|public
specifier|static
specifier|final
name|String
name|SELF
init|=
literal|"_self"
decl_stmt|;
comment|/** Opens link in parent frame. */
DECL|field|PARENT
specifier|public
specifier|static
specifier|final
name|String
name|PARENT
init|=
literal|"_parent"
decl_stmt|;
comment|/** Opens link in the full body of the window. */
DECL|field|TOP
specifier|public
specifier|static
specifier|final
name|String
name|TOP
init|=
literal|"_top"
decl_stmt|;
block|}
block|}
end_interface

end_unit

