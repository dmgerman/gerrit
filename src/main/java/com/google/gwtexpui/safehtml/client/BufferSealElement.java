begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright 2009 Google Inc.
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
DECL|package|com.google.gwtexpui.safehtml.client
package|package
name|com
operator|.
name|google
operator|.
name|gwtexpui
operator|.
name|safehtml
operator|.
name|client
package|;
end_package

begin_class
DECL|class|BufferSealElement
class|class
name|BufferSealElement
implements|implements
name|Buffer
block|{
DECL|field|shb
specifier|private
specifier|final
name|SafeHtmlBuilder
name|shb
decl_stmt|;
DECL|method|BufferSealElement (final SafeHtmlBuilder safeHtmlBuilder)
name|BufferSealElement
parameter_list|(
specifier|final
name|SafeHtmlBuilder
name|safeHtmlBuilder
parameter_list|)
block|{
name|shb
operator|=
name|safeHtmlBuilder
expr_stmt|;
block|}
DECL|method|append (final boolean v)
specifier|public
name|void
name|append
parameter_list|(
specifier|final
name|boolean
name|v
parameter_list|)
block|{
name|shb
operator|.
name|sealElement
argument_list|()
operator|.
name|append
argument_list|(
name|v
argument_list|)
expr_stmt|;
block|}
DECL|method|append (final char v)
specifier|public
name|void
name|append
parameter_list|(
specifier|final
name|char
name|v
parameter_list|)
block|{
name|shb
operator|.
name|sealElement
argument_list|()
operator|.
name|append
argument_list|(
name|v
argument_list|)
expr_stmt|;
block|}
DECL|method|append (final double v)
specifier|public
name|void
name|append
parameter_list|(
specifier|final
name|double
name|v
parameter_list|)
block|{
name|shb
operator|.
name|sealElement
argument_list|()
operator|.
name|append
argument_list|(
name|v
argument_list|)
expr_stmt|;
block|}
DECL|method|append (final float v)
specifier|public
name|void
name|append
parameter_list|(
specifier|final
name|float
name|v
parameter_list|)
block|{
name|shb
operator|.
name|sealElement
argument_list|()
operator|.
name|append
argument_list|(
name|v
argument_list|)
expr_stmt|;
block|}
DECL|method|append (final int v)
specifier|public
name|void
name|append
parameter_list|(
specifier|final
name|int
name|v
parameter_list|)
block|{
name|shb
operator|.
name|sealElement
argument_list|()
operator|.
name|append
argument_list|(
name|v
argument_list|)
expr_stmt|;
block|}
DECL|method|append (final long v)
specifier|public
name|void
name|append
parameter_list|(
specifier|final
name|long
name|v
parameter_list|)
block|{
name|shb
operator|.
name|sealElement
argument_list|()
operator|.
name|append
argument_list|(
name|v
argument_list|)
expr_stmt|;
block|}
DECL|method|append (final String v)
specifier|public
name|void
name|append
parameter_list|(
specifier|final
name|String
name|v
parameter_list|)
block|{
name|shb
operator|.
name|sealElement
argument_list|()
operator|.
name|append
argument_list|(
name|v
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|shb
operator|.
name|sealElement
argument_list|()
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

