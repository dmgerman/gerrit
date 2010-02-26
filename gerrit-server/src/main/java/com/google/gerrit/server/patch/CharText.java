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
DECL|package|com.google.gerrit.server.patch
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|patch
package|;
end_package

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|diff
operator|.
name|Sequence
import|;
end_import

begin_class
DECL|class|CharText
class|class
name|CharText
implements|implements
name|Sequence
block|{
DECL|field|content
specifier|private
specifier|final
name|String
name|content
decl_stmt|;
DECL|method|CharText (Text text, int s, int e)
name|CharText
parameter_list|(
name|Text
name|text
parameter_list|,
name|int
name|s
parameter_list|,
name|int
name|e
parameter_list|)
block|{
name|content
operator|=
name|text
operator|.
name|getLines
argument_list|(
name|s
argument_list|,
name|e
argument_list|,
literal|false
comment|/* keep LF */
argument_list|)
expr_stmt|;
block|}
DECL|method|charAt (int idx)
name|char
name|charAt
parameter_list|(
name|int
name|idx
parameter_list|)
block|{
return|return
name|content
operator|.
name|charAt
argument_list|(
name|idx
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|equals (int a, Sequence other, int b)
specifier|public
name|boolean
name|equals
parameter_list|(
name|int
name|a
parameter_list|,
name|Sequence
name|other
parameter_list|,
name|int
name|b
parameter_list|)
block|{
return|return
name|content
operator|.
name|charAt
argument_list|(
name|a
argument_list|)
operator|==
operator|(
operator|(
name|CharText
operator|)
name|other
operator|)
operator|.
name|content
operator|.
name|charAt
argument_list|(
name|b
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|size ()
specifier|public
name|int
name|size
parameter_list|()
block|{
return|return
name|content
operator|.
name|length
argument_list|()
return|;
block|}
block|}
end_class

end_unit

