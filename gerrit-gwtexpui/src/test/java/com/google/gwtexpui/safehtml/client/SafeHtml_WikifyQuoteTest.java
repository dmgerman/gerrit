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
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertNotSame
import|;
end_import

begin_class
DECL|class|SafeHtml_WikifyQuoteTest
specifier|public
class|class
name|SafeHtml_WikifyQuoteTest
block|{
DECL|field|B
specifier|private
specifier|static
specifier|final
name|String
name|B
init|=
literal|"<blockquote class=\"wikiQuote\">"
decl_stmt|;
DECL|field|E
specifier|private
specifier|static
specifier|final
name|String
name|E
init|=
literal|"</blockquote>"
decl_stmt|;
DECL|method|quote (String raw)
specifier|private
specifier|static
name|String
name|quote
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
annotation|@
name|Test
DECL|method|testQuote1 ()
specifier|public
name|void
name|testQuote1
parameter_list|()
block|{
specifier|final
name|SafeHtml
name|o
init|=
name|html
argument_list|(
literal|"> I'm happy\n> with quotes!\n\nSee above."
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
name|quote
argument_list|(
literal|"I&#39;m happy\nwith quotes!"
argument_list|)
operator|+
literal|"<p>See above.</p>"
argument_list|,
name|n
operator|.
name|asString
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testQuote2 ()
specifier|public
name|void
name|testQuote2
parameter_list|()
block|{
specifier|final
name|SafeHtml
name|o
init|=
name|html
argument_list|(
literal|"See this said:\n\n> a quoted\n> string block\n\nOK?"
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
literal|"<p>See this said:</p>"
operator|+
name|quote
argument_list|(
literal|"a quoted\nstring block"
argument_list|)
operator|+
literal|"<p>OK?</p>"
argument_list|,
name|n
operator|.
name|asString
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testNestedQuotes1 ()
specifier|public
name|void
name|testNestedQuotes1
parameter_list|()
block|{
specifier|final
name|SafeHtml
name|o
init|=
name|html
argument_list|(
literal|">> prior\n> \n> next\n"
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
name|assertEquals
argument_list|(
name|quote
argument_list|(
name|quote
argument_list|(
literal|"prior"
argument_list|)
operator|+
literal|"next\n"
argument_list|)
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

