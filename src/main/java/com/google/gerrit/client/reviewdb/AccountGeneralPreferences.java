begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2008 The Android Open Source Project
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
DECL|package|com.google.gerrit.client.reviewdb
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|reviewdb
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|client
operator|.
name|Column
import|;
end_import

begin_comment
comment|/** Preferences about a single user. */
end_comment

begin_class
DECL|class|AccountGeneralPreferences
specifier|public
specifier|final
class|class
name|AccountGeneralPreferences
block|{
comment|/** Default number of lines of context. */
DECL|field|DEFAULT_CONTEXT
specifier|public
specifier|static
specifier|final
name|short
name|DEFAULT_CONTEXT
init|=
literal|10
decl_stmt|;
comment|/** Context setting to display the entire file. */
DECL|field|WHOLE_FILE_CONTEXT
specifier|public
specifier|static
specifier|final
name|short
name|WHOLE_FILE_CONTEXT
init|=
operator|-
literal|1
decl_stmt|;
comment|/** Typical valid choices for the default context setting. */
DECL|field|CONTEXT_CHOICES
specifier|public
specifier|static
specifier|final
name|short
index|[]
name|CONTEXT_CHOICES
init|=
block|{
literal|3
block|,
literal|10
block|,
literal|25
block|,
literal|50
block|,
literal|75
block|,
literal|100
block|,
name|WHOLE_FILE_CONTEXT
block|}
decl_stmt|;
comment|/** Default number of lines of context when viewing a patch. */
annotation|@
name|Column
DECL|field|defaultContext
specifier|protected
name|short
name|defaultContext
decl_stmt|;
comment|/** Should the site header be displayed when logged in ? */
annotation|@
name|Column
DECL|field|showSiteHeader
specifier|protected
name|boolean
name|showSiteHeader
decl_stmt|;
comment|/** Should the Flash helper movie be used to copy text to the clipboard? */
annotation|@
name|Column
DECL|field|useFlashClipboard
specifier|protected
name|boolean
name|useFlashClipboard
decl_stmt|;
DECL|method|AccountGeneralPreferences ()
specifier|public
name|AccountGeneralPreferences
parameter_list|()
block|{   }
comment|/** Get the default number of lines of context when viewing a patch. */
DECL|method|getDefaultContext ()
specifier|public
name|short
name|getDefaultContext
parameter_list|()
block|{
return|return
name|defaultContext
return|;
block|}
comment|/** Set the number of lines of context when viewing a patch. */
DECL|method|setDefaultContext (final short s)
specifier|public
name|void
name|setDefaultContext
parameter_list|(
specifier|final
name|short
name|s
parameter_list|)
block|{
name|defaultContext
operator|=
name|s
expr_stmt|;
block|}
DECL|method|isShowSiteHeader ()
specifier|public
name|boolean
name|isShowSiteHeader
parameter_list|()
block|{
return|return
name|showSiteHeader
return|;
block|}
DECL|method|setShowSiteHeader (final boolean b)
specifier|public
name|void
name|setShowSiteHeader
parameter_list|(
specifier|final
name|boolean
name|b
parameter_list|)
block|{
name|showSiteHeader
operator|=
name|b
expr_stmt|;
block|}
DECL|method|isUseFlashClipboard ()
specifier|public
name|boolean
name|isUseFlashClipboard
parameter_list|()
block|{
return|return
name|useFlashClipboard
return|;
block|}
DECL|method|setUseFlashClipboard (final boolean b)
specifier|public
name|void
name|setUseFlashClipboard
parameter_list|(
specifier|final
name|boolean
name|b
parameter_list|)
block|{
name|useFlashClipboard
operator|=
name|b
expr_stmt|;
block|}
DECL|method|resetToDefaults ()
specifier|public
name|void
name|resetToDefaults
parameter_list|()
block|{
name|defaultContext
operator|=
name|DEFAULT_CONTEXT
expr_stmt|;
name|showSiteHeader
operator|=
literal|true
expr_stmt|;
name|useFlashClipboard
operator|=
literal|true
expr_stmt|;
block|}
block|}
end_class

end_unit

