begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2010 The Android Open Source Project
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
DECL|package|com.google.gerrit.prettify.common
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|prettify
operator|.
name|common
package|;
end_package

begin_comment
comment|/** Settings to configure a {@link PrettyFormatter}. */
end_comment

begin_class
DECL|class|PrettySettings
specifier|public
class|class
name|PrettySettings
block|{
DECL|field|fileName
specifier|protected
name|String
name|fileName
decl_stmt|;
DECL|field|showWhiteSpaceErrors
specifier|protected
name|boolean
name|showWhiteSpaceErrors
decl_stmt|;
DECL|field|lineLength
specifier|protected
name|int
name|lineLength
decl_stmt|;
DECL|field|tabSize
specifier|protected
name|int
name|tabSize
decl_stmt|;
DECL|field|showTabs
specifier|protected
name|boolean
name|showTabs
decl_stmt|;
DECL|method|PrettySettings ()
specifier|public
name|PrettySettings
parameter_list|()
block|{
name|showWhiteSpaceErrors
operator|=
literal|true
expr_stmt|;
name|lineLength
operator|=
literal|100
expr_stmt|;
name|tabSize
operator|=
literal|2
expr_stmt|;
name|showTabs
operator|=
literal|true
expr_stmt|;
block|}
DECL|method|PrettySettings (PrettySettings pretty)
specifier|public
name|PrettySettings
parameter_list|(
name|PrettySettings
name|pretty
parameter_list|)
block|{
name|fileName
operator|=
name|pretty
operator|.
name|fileName
expr_stmt|;
name|showWhiteSpaceErrors
operator|=
name|pretty
operator|.
name|showWhiteSpaceErrors
expr_stmt|;
name|lineLength
operator|=
name|pretty
operator|.
name|lineLength
expr_stmt|;
name|tabSize
operator|=
name|pretty
operator|.
name|tabSize
expr_stmt|;
name|showTabs
operator|=
name|pretty
operator|.
name|showTabs
expr_stmt|;
block|}
DECL|method|getFilename ()
specifier|public
name|String
name|getFilename
parameter_list|()
block|{
return|return
name|fileName
return|;
block|}
DECL|method|setFileName (final String name)
specifier|public
name|PrettySettings
name|setFileName
parameter_list|(
specifier|final
name|String
name|name
parameter_list|)
block|{
name|fileName
operator|=
name|name
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|isShowWhiteSpaceErrors ()
specifier|public
name|boolean
name|isShowWhiteSpaceErrors
parameter_list|()
block|{
return|return
name|showWhiteSpaceErrors
return|;
block|}
DECL|method|setShowWhiteSpaceErrors (final boolean show)
specifier|public
name|PrettySettings
name|setShowWhiteSpaceErrors
parameter_list|(
specifier|final
name|boolean
name|show
parameter_list|)
block|{
name|showWhiteSpaceErrors
operator|=
name|show
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|getLineLength ()
specifier|public
name|int
name|getLineLength
parameter_list|()
block|{
return|return
name|lineLength
return|;
block|}
DECL|method|setLineLength (final int len)
specifier|public
name|PrettySettings
name|setLineLength
parameter_list|(
specifier|final
name|int
name|len
parameter_list|)
block|{
name|lineLength
operator|=
name|len
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|getTabSize ()
specifier|public
name|int
name|getTabSize
parameter_list|()
block|{
return|return
name|tabSize
return|;
block|}
DECL|method|setTabSize (final int len)
specifier|public
name|PrettySettings
name|setTabSize
parameter_list|(
specifier|final
name|int
name|len
parameter_list|)
block|{
name|tabSize
operator|=
name|len
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|isShowTabs ()
specifier|public
name|boolean
name|isShowTabs
parameter_list|()
block|{
return|return
name|showTabs
return|;
block|}
DECL|method|setShowTabs (final boolean show)
specifier|public
name|PrettySettings
name|setShowTabs
parameter_list|(
specifier|final
name|boolean
name|show
parameter_list|)
block|{
name|showTabs
operator|=
name|show
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
end_class

end_unit

