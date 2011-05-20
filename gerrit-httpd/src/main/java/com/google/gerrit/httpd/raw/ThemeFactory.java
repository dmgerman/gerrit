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
DECL|package|com.google.gerrit.httpd.raw
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|httpd
operator|.
name|raw
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
name|HostPageData
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
name|config
operator|.
name|GerritServerConfig
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
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|Config
import|;
end_import

begin_class
DECL|class|ThemeFactory
class|class
name|ThemeFactory
block|{
DECL|field|cfg
specifier|private
specifier|final
name|Config
name|cfg
decl_stmt|;
annotation|@
name|Inject
DECL|method|ThemeFactory (@erritServerConfig Config cfg)
name|ThemeFactory
parameter_list|(
annotation|@
name|GerritServerConfig
name|Config
name|cfg
parameter_list|)
block|{
name|this
operator|.
name|cfg
operator|=
name|cfg
expr_stmt|;
block|}
DECL|method|getSignedOutTheme ()
name|HostPageData
operator|.
name|Theme
name|getSignedOutTheme
parameter_list|()
block|{
return|return
name|getTheme
argument_list|(
literal|"signed-out"
argument_list|)
return|;
block|}
DECL|method|getSignedInTheme ()
name|HostPageData
operator|.
name|Theme
name|getSignedInTheme
parameter_list|()
block|{
return|return
name|getTheme
argument_list|(
literal|"signed-in"
argument_list|)
return|;
block|}
DECL|method|getTheme (String name)
specifier|private
name|HostPageData
operator|.
name|Theme
name|getTheme
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|HostPageData
operator|.
name|Theme
name|theme
init|=
operator|new
name|HostPageData
operator|.
name|Theme
argument_list|()
decl_stmt|;
name|theme
operator|.
name|backgroundColor
operator|=
name|color
argument_list|(
name|name
argument_list|,
literal|"backgroundColor"
argument_list|,
literal|"#FFFFFF"
argument_list|)
expr_stmt|;
name|theme
operator|.
name|textColor
operator|=
name|color
argument_list|(
name|name
argument_list|,
literal|"textColor"
argument_list|,
literal|"#000000"
argument_list|)
expr_stmt|;
name|theme
operator|.
name|trimColor
operator|=
name|color
argument_list|(
name|name
argument_list|,
literal|"trimColor"
argument_list|,
literal|"#D4E9A9"
argument_list|)
expr_stmt|;
name|theme
operator|.
name|selectionColor
operator|=
name|color
argument_list|(
name|name
argument_list|,
literal|"selectionColor"
argument_list|,
literal|"#FFFFCC"
argument_list|)
expr_stmt|;
name|theme
operator|.
name|topMenuColor
operator|=
name|color
argument_list|(
name|name
argument_list|,
literal|"topMenuColor"
argument_list|,
name|theme
operator|.
name|trimColor
argument_list|)
expr_stmt|;
return|return
name|theme
return|;
block|}
DECL|method|color (String section, String name, String defaultValue)
specifier|private
name|String
name|color
parameter_list|(
name|String
name|section
parameter_list|,
name|String
name|name
parameter_list|,
name|String
name|defaultValue
parameter_list|)
block|{
name|String
name|v
init|=
name|cfg
operator|.
name|getString
argument_list|(
literal|"theme"
argument_list|,
name|section
argument_list|,
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|v
operator|==
literal|null
operator|||
name|v
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|v
operator|=
name|cfg
operator|.
name|getString
argument_list|(
literal|"theme"
argument_list|,
literal|null
argument_list|,
name|name
argument_list|)
expr_stmt|;
if|if
condition|(
name|v
operator|==
literal|null
operator|||
name|v
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
if|if
condition|(
literal|"signed-in"
operator|.
name|equals
argument_list|(
name|section
argument_list|)
operator|&&
literal|"backgroundColor"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|v
operator|=
literal|"#FCFEEF"
expr_stmt|;
block|}
else|else
block|{
name|v
operator|=
name|defaultValue
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
operator|!
name|v
operator|.
name|startsWith
argument_list|(
literal|"#"
argument_list|)
operator|&&
name|v
operator|.
name|matches
argument_list|(
literal|"^[0-9a-fA-F]{2,6}$"
argument_list|)
condition|)
block|{
name|v
operator|=
literal|"#"
operator|+
name|v
expr_stmt|;
block|}
return|return
name|v
return|;
block|}
block|}
end_class

end_unit

