begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2013 The Android Open Source Project
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

begin_class
DECL|class|ThemeInfo
specifier|public
class|class
name|ThemeInfo
block|{
DECL|field|INHERIT
specifier|static
specifier|final
name|ThemeInfo
name|INHERIT
init|=
operator|new
name|ThemeInfo
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
decl_stmt|;
DECL|field|css
specifier|public
specifier|final
name|String
name|css
decl_stmt|;
DECL|field|header
specifier|public
specifier|final
name|String
name|header
decl_stmt|;
DECL|field|footer
specifier|public
specifier|final
name|String
name|footer
decl_stmt|;
DECL|method|ThemeInfo (String css, String header, String footer)
name|ThemeInfo
parameter_list|(
name|String
name|css
parameter_list|,
name|String
name|header
parameter_list|,
name|String
name|footer
parameter_list|)
block|{
name|this
operator|.
name|css
operator|=
name|css
expr_stmt|;
name|this
operator|.
name|header
operator|=
name|header
expr_stmt|;
name|this
operator|.
name|footer
operator|=
name|footer
expr_stmt|;
block|}
block|}
end_class

end_unit

