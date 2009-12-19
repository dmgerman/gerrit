begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2009 The Android Open Source Project
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
comment|// distributed under the License is distributed on an "<p>AS IS" BASIS,
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

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|TestCase
import|;
end_import

begin_class
DECL|class|SafeHtml_WikifyPreformatTest
specifier|public
class|class
name|SafeHtml_WikifyPreformatTest
extends|extends
name|TestCase
block|{
DECL|field|B
specifier|private
specifier|static
specifier|final
name|String
name|B
init|=
literal|"<span class=\"wikiPreFormat\">"
decl_stmt|;
DECL|field|E
specifier|private
specifier|static
specifier|final
name|String
name|E
init|=
literal|"</span><br />"
decl_stmt|;
DECL|method|pre (String raw)
specifier|private
specifier|static
name|String
name|pre
parameter_list|(
name|String
name|raw
parameter_list|)
block|{
return|return
name|B
operator|+
name|raw
operator|+
name|E
return|;
block|}
DECL|method|testPreformat1 ()
specifier|public
name|void
name|testPreformat1
parameter_list|()
block|{
specifier|final
name|SafeHtml
name|o
init|=
name|html
argument_list|(
literal|"A\n\n  This is pre\n  formatted"
argument_list|)
decl_stmt|;
specifier|final
name|SafeHtml
name|n
init|=
name|o
operator|.
name|wikify
argument_list|()
decl_stmt|;
name|assertNotSame
argument_list|(
name|o
argument_list|,
name|n
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"<p>A</p>"
comment|//
operator|+
literal|"<p>"
comment|//
operator|+
name|pre
argument_list|(
literal|"  This is pre"
argument_list|)
comment|//
operator|+
name|pre
argument_list|(
literal|"  formatted"
argument_list|)
comment|//
operator|+
literal|"</p>"
comment|//
argument_list|,
name|n
operator|.
name|asString
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testPreformat2 ()
specifier|public
name|void
name|testPreformat2
parameter_list|()
block|{
specifier|final
name|SafeHtml
name|o
init|=
name|html
argument_list|(
literal|"A\n\n  This is pre\n  formatted\n\nbut this is not"
argument_list|)
decl_stmt|;
specifier|final
name|SafeHtml
name|n
init|=
name|o
operator|.
name|wikify
argument_list|()
decl_stmt|;
name|assertNotSame
argument_list|(
name|o
argument_list|,
name|n
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"<p>A</p>"
comment|//
operator|+
literal|"<p>"
comment|//
operator|+
name|pre
argument_list|(
literal|"  This is pre"
argument_list|)
comment|//
operator|+
name|pre
argument_list|(
literal|"  formatted"
argument_list|)
comment|//
operator|+
literal|"</p>"
comment|//
operator|+
literal|"<p>but this is not</p>"
comment|//
argument_list|,
name|n
operator|.
name|asString
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testPreformat3 ()
specifier|public
name|void
name|testPreformat3
parameter_list|()
block|{
specifier|final
name|SafeHtml
name|o
init|=
name|html
argument_list|(
literal|"A\n\n  Q\n<R>\n  S\n\nB"
argument_list|)
decl_stmt|;
specifier|final
name|SafeHtml
name|n
init|=
name|o
operator|.
name|wikify
argument_list|()
decl_stmt|;
name|assertNotSame
argument_list|(
name|o
argument_list|,
name|n
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"<p>A</p>"
comment|//
operator|+
literal|"<p>"
comment|//
operator|+
name|pre
argument_list|(
literal|"  Q"
argument_list|)
comment|//
operator|+
name|pre
argument_list|(
literal|"&lt;R&gt;"
argument_list|)
comment|//
operator|+
name|pre
argument_list|(
literal|"  S"
argument_list|)
comment|//
operator|+
literal|"</p>"
comment|//
operator|+
literal|"<p>B</p>"
comment|//
argument_list|,
name|n
operator|.
name|asString
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testPreformat4 ()
specifier|public
name|void
name|testPreformat4
parameter_list|()
block|{
specifier|final
name|SafeHtml
name|o
init|=
name|html
argument_list|(
literal|"  Q\n<R>\n  S\n\nB"
argument_list|)
decl_stmt|;
specifier|final
name|SafeHtml
name|n
init|=
name|o
operator|.
name|wikify
argument_list|()
decl_stmt|;
name|assertNotSame
argument_list|(
name|o
argument_list|,
name|n
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"<p>"
comment|//
operator|+
name|pre
argument_list|(
literal|"  Q"
argument_list|)
comment|//
operator|+
name|pre
argument_list|(
literal|"&lt;R&gt;"
argument_list|)
comment|//
operator|+
name|pre
argument_list|(
literal|"  S"
argument_list|)
comment|//
operator|+
literal|"</p>"
comment|//
operator|+
literal|"<p>B</p>"
comment|//
argument_list|,
name|n
operator|.
name|asString
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|html (String text)
specifier|private
specifier|static
name|SafeHtml
name|html
parameter_list|(
name|String
name|text
parameter_list|)
block|{
return|return
operator|new
name|SafeHtmlBuilder
argument_list|()
operator|.
name|append
argument_list|(
name|text
argument_list|)
operator|.
name|toSafeHtml
argument_list|()
return|;
block|}
block|}
end_class

end_unit

